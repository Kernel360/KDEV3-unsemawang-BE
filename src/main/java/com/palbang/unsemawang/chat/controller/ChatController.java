package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.request.ChatMessageRequest;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chat.service.ChatMessageProducer;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@Controller
@AllArgsConstructor
public class ChatController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ChatMessageProducer chatMessageProducer;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;

	@Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 메시지를 전송하고 RabbitMQ로 전달합니다.")
	@MessageMapping("/chat/{id}")
	public void sendMessage(
		StompHeaderAccessor stompHeaderAccessor,
		@Payload ChatMessageRequest chatMessageRequest,
		@DestinationVariable("id") Long chatRoomId
	) {

		log.info("WebSocket 메시지 수신: 채팅방 ID={}, 메시지={}", chatRoomId, chatMessageRequest.getMessage());

		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		String memberId = getSessionUserId(stompHeaderAccessor);
		Member sender = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(
				ResponseCode.RESOURCE_NOT_FOUND,
				"발신자를 찾을 수 없습니다. senderId=" + memberId
			));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(
				ResponseCode.RESOURCE_NOT_FOUND,
				"채팅방을 찾을 수 없습니다. chatRoomId=" + chatRoomId
			));

		if (chatRoom.getUser1() == null || chatRoom.getUser2() == null) {
			throw new GeneralException(ResponseCode.FORBIDDEN, "채팅방에서 메시지 입력이 차단되었습니다. 혼자 남은 상태입니다.");
		}

		ChatMessageDto chatMessageDto = createChatMessageDto(chatMessageRequest.getMessage(), chatRoomId, sender);

		try {
			chatMessageProducer.sendMessageToQueue(chatMessageDto);
		} catch (Exception e) {
			throw new GeneralException(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR, "메시지를 RabbitMQ로 전송하는 데 실패했습니다.", e);
		}
	}

	private ChatMessageDto createChatMessageDto(String message, Long chatRoomId, Member sender) {
		return ChatMessageDto.builder()
			.chatRoomId(chatRoomId)
			.senderId(sender.getId())
			.content(message)
			.nickname(sender.getNickname())
			.profileImageUrl(sender.getProfileUrl())
			.senderType(SenderType.SELF)
			.build();
	}

	private String getSessionUserId(StompHeaderAccessor stompHeaderAccessor) {

		// 회원 ID 추출
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)stompHeaderAccessor.getUser();

		if (auth == null || auth.getPrincipal() == null) {
			return null;
		}

		return ((CustomOAuth2User)auth.getPrincipal()).getId();

	}
}
