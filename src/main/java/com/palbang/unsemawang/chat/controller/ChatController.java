package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.NewChatMessageCountDto;
import com.palbang.unsemawang.chat.dto.NewChatMessageDto;
import com.palbang.unsemawang.chat.dto.request.ChatMessageRequest;
import com.palbang.unsemawang.chat.service.ChatMessageService;
import com.palbang.unsemawang.member.entity.Member;
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
	private final ChatMessageService chatMessageService;

	@Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 메시지를 전송하고 RabbitMQ로 전달합니다.")
	@MessageMapping("/chat/{id}")
	public void sendMessage(
		StompHeaderAccessor stompHeaderAccessor,
		@Payload ChatMessageRequest chatMessageRequest,
		@DestinationVariable("id") Long chatRoomId
	) {

		log.info("WebSocket 메시지 수신: 채팅방 ID={}, 메시지={}", chatRoomId, chatMessageRequest.getMessage());

		String memberId = getSessionUserId(stompHeaderAccessor);

		ChatMessageDto chatMessageDto = chatMessageService.saveChatMessage(memberId, chatRoomId, chatMessageRequest);

		simpMessageSendingOperations.convertAndSend("/topic/chat/" + chatMessageDto.getChatRoomId(), chatMessageDto);
		log.info("Forwarded WebSocket message: {}", chatMessageDto);

		// 새로운 메세지 내용과 안본 메세지 수를 보냄
		String newMessageDestination =
			"/topic/chat/" + chatMessageDto.getChatRoomId() + "/" + chatMessageDto.getSenderId() + "/new-message";
		simpMessageSendingOperations.convertAndSend(newMessageDestination,
			NewChatMessageDto.of(chatMessageDto.getContent()));

		int count = chatMessageService.getNotReadMessageOfPartner(chatMessageDto.getSenderId(),
			chatMessageDto.getChatRoomId());
		simpMessageSendingOperations.convertAndSend(newMessageDestination + "/count", NewChatMessageCountDto.of(count));
	}

	private ChatMessageDto createChatMessageDto(String message, Long chatRoomId, Member sender) {
		return ChatMessageDto.builder()
			.chatRoomId(chatRoomId)
			.senderId(sender.getId())
			.content(message)
			.nickname(sender.getNickname())
			.profileImageUrl(sender.getProfileUrl())
			//			.senderType(SenderType.SELF)
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
