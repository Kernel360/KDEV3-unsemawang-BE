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

	// ✅ WebSocket 메시지 핸들링
	@MessageMapping("chat/sendMessage") // 🔥 슬래시 없이 사용
	public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
		log.info("📩 Received WebSocket message: {}", chatMessageDto);

		// ✅ chatRoomId가 없으면 메시지 무시
		if (chatMessageDto.getChatRoomId() == null) {
			log.error("❌ chatRoomId가 누락됨! {}", chatMessageDto);
			return;
		}

		// ✅ 메시지를 RabbitMQ로 전송
		chatMessageProducer.sendMessageToQueue(chatMessageDto);
	}
}
