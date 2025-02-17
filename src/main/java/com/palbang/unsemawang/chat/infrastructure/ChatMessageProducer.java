package com.palbang.unsemawang.chat.infrastructure;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.application.dto.ChatMessageResponseDto;
import com.palbang.unsemawang.chat.constant.MessageStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageProducer {

	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

	/**
	 * 메시지를 WebSocket과 Redis에 저장
	 */
	public void sendMessage(ChatMessageResponseDto message) {
		try {
			Long chatRoomId = message.getChatRoomId();

			// 상태 변경 (WebSocket 전송 전)
			message = message.toBuilder().status(MessageStatus.RECEIVED).build();

			// WebSocket을 통해 즉시 전송
			messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, message);
			log.info("WebSocket으로 메시지 전송: {}", message);
		} catch (Exception e) {
			log.error("메시지 처리 중 오류 발생", e);
		}
	}

	// private final RabbitTemplate rabbitTemplate;
	// private final ObjectMapper objectMapper;
	//
	// private static final String EXCHANGE_NAME = "chat.exchange";
	// private static final String ROUTING_KEY = "chat.routing.key";
	//
	// /**
	//  * 메시지를 RabbitMQ 큐에 저장
	//  */
	// public void sendMessageToQueue(ChatMessageInfo chatMessageInfo) {
	// 	try {
	// 		// JSON 변환
	// 		String messageJson = objectMapper.writeValueAsString(chatMessageInfo);
	// 		log.info("RabbitMQ로 메시지 전송: 메시지={}", messageJson);
	//
	// 		// RabbitMQ로 메시지 전송
	// 		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, messageJson);
	// 	} catch (Exception e) {
	// 		log.error("RabbitMQ 메시지 전송 실패", e);
	// 	}
	// }
}
