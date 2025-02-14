package com.palbang.unsemawang.chat.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.palbang.unsemawang.activity.constant.ActiveStatus;
import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.NewChatMessageCountDto;
import com.palbang.unsemawang.chat.dto.NewChatMessageDto;
import com.palbang.unsemawang.chat.dto.request.ChatMessageRequest;
import com.palbang.unsemawang.chat.service.ChatMessageService;
import com.palbang.unsemawang.fcm.dto.request.FcmNotificationRequest;
import com.palbang.unsemawang.fcm.service.FcmService;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.service.MemberService;
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
	private final FcmService fcmService;
	private final MemberService memberService;

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

		// 채팅 상대방이 현재 채팅방을 보고 있는지 보고있다면 어디 채팅방을 보고 있는지 확인해서 알림 여부 결정
		// 채팅방 본인과의 채팅방번호를 보고 있다면 알림

		//수신자 id를 가져옴
		String receiverId = chatMessageService.getReceiverId(memberId, chatRoomId);
		//
		System.out.println("receiverId: " + receiverId);
		//ActiveMember status = chatMessageService.getReceiverIdStatus(receiverId, chatRoomId);
		String nickname = memberService.getMemberProfile(memberId).getNickname();
		String message = chatMessageRequest.getMessage();
		String url = "https://www.unsemawang.com/chat/"+chatRoomId;
		//receiverId의 fcm 토큰 조회해서 전부 메시지 전송 요청
		List<String> fcmList = fcmService.getFcmToken(receiverId);

			for(String fcmToken : fcmList) {
				FcmNotificationRequest request = new FcmNotificationRequest(fcmToken,nickname,message,url);
				try{
					fcmService.sendPushMessage(request);
				}catch(FirebaseMessagingException e){
					e.printStackTrace();
				}
			}


		// 새로운 메세지 내용과 안본 메세지 수를 보냄
		String newMessageDestination =
			"/topic/chat/" + chatMessageDto.getChatRoomId() + "/" + chatMessageDto.getSenderId() + "/new-message";
		simpMessageSendingOperations.convertAndSend(newMessageDestination,
			NewChatMessageDto.of(
				chatMessageDto.getContent(),
				LocalDateTime.ofInstant(Instant.ofEpochMilli(chatMessageDto.getTimestamp()), ZoneId.systemDefault())
			)
		);

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
