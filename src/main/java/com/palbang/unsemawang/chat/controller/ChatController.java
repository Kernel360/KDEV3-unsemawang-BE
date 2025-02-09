package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.service.ChatMessageProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatController {

	private final ChatMessageProducer chatMessageProducer;

	public ChatController(ChatMessageProducer chatMessageProducer) {
		this.chatMessageProducer = chatMessageProducer;
	}

	@MessageMapping("/chat/sendMessage")  // ✅ 경로 앞에 '/' 추가!
	public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
		log.info("📩 Received WebSocket message: {}", chatMessageDto);

		if (chatMessageDto.getChatRoomId() == null) {
			log.error("❌ chatRoomId가 누락됨! {}", chatMessageDto);
			return;
		}

		try {
			log.info("📨 Sending message to RabbitMQ: {}", chatMessageDto);
			chatMessageProducer.sendMessageToQueue(chatMessageDto);
			log.info("✅ Message sent to RabbitMQ successfully!");
		} catch (Exception e) {
			log.error("❌ Failed to send message to RabbitMQ", e);
		}
	}
}
