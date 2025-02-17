package com.palbang.unsemawang.chat.presentaion;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.application.ChatFacade;
import com.palbang.unsemawang.chat.application.dto.ChatMessageRequestDto;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatFacade chatFacade;

	/**
	 * WebSocket을 통해 메시지를 받고 RabbitMQ를 통해 비동기적으로 처리
	 */
	@Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 메시지를 전송하고 RabbitMQ로 전달합니다.")
	@MessageMapping("/chat/{id}")
	public void sendMessage(
		StompHeaderAccessor stompHeaderAccessor,
		@Payload ChatMessageRequestDto chatMessageRequestDto,
		@DestinationVariable("id") Long chatRoomId) {

		// 헤더에서 사용자 ID 가져오기 (JWT 기반 인증)
		String senderId = getSessionUserId(stompHeaderAccessor);
		if (senderId == null) {
			log.error("WebSocket 인증 실패: 사용자 정보 없음");
			return;
		}

		log.info("WebSocket 메시지 수신: 채팅방 ID={}, 발신자={}, 메시지={}",
			chatRoomId, senderId, chatMessageRequestDto.getMessage());

		// 메시지 처리 → RabbitMQ를 통해 메시지 큐에 저장
		chatFacade.processMessage(chatRoomId, senderId, chatMessageRequestDto);
	}

	/**
	 * STOMP 헤더에서 사용자 ID(JWT 기반) 추출
	 */
	private String getSessionUserId(StompHeaderAccessor stompHeaderAccessor) {
		UsernamePasswordAuthenticationToken auth =
			(UsernamePasswordAuthenticationToken)stompHeaderAccessor.getUser();

		if (auth == null || auth.getPrincipal() == null) {
			return null;
		}

		return ((CustomOAuth2User)auth.getPrincipal()).getId();
	}
}
