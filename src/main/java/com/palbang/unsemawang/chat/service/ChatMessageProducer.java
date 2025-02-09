package com.palbang.unsemawang.chat.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatMessageProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	public ChatMessageProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
		this.rabbitTemplate = rabbitTemplate;
		this.objectMapper = objectMapper;
	}

	public void sendMessageToQueue(ChatMessageDto chatMessage) {
		try {
			// ✅ chatRoomId가 없으면 전송하지 않음
			if (chatMessage.getChatRoomId() == null) {
				log.error("❌ chatRoomId가 없는 메시지는 전송 불가: {}", chatMessage);
				return;
			}

			// ✅ timestamp가 없으면 자동 설정
			if (chatMessage.getTimestamp() == null) {
				chatMessage.setTimestamp(System.currentTimeMillis());
			}

			String messageJson = objectMapper.writeValueAsString(chatMessage);
			log.info("📩 Sending message to RabbitMQ: {}", messageJson);
			rabbitTemplate.convertAndSend("chat.exchange", "chat.routing.key", messageJson);
		} catch (Exception e) {
			log.error("❌ 메시지 직렬화 실패", e);
		}
	}
}

