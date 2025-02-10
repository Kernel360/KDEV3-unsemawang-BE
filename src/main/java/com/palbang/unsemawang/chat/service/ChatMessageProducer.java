package com.palbang.unsemawang.chat.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ChatMessageProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	public void sendMessageToQueue(ChatMessageDto chatMessage) {
		try {
			if (chatMessage.getChatRoomId() == null) {
				log.error("chatRoomId가 없는 메시지는 전송 불가: {}", chatMessage);
				return;
			}

			// LocalDateTime이 null이면 현재 시간을 설정
			if (chatMessage.getTimestamp() == null) {
				chatMessage.setTimestamp(System.currentTimeMillis());  // long 유지
			}

			String messageJson = objectMapper.writeValueAsString(chatMessage);
			log.info("Sending message to RabbitMQ: {}", messageJson);
			rabbitTemplate.convertAndSend("chat.exchange", "chat.routing.key", messageJson);
		} catch (Exception e) {
			log.error("메시지 직렬화 실패", e);
		}
	}
}
