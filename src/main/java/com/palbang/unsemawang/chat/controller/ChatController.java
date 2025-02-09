package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

	@MessageMapping("chat/sendMessage")
	public void sendMessage(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
		log.info("📩 Received WebSocket message: {}", chatMessageDto);

		// ✅ WebSocket 세션에서 senderId 가져오기
		String senderId = (String)headerAccessor.getSessionAttributes().get("userId");

		if (senderId == null) {
			log.error("❌ WebSocket 세션에서 senderId를 찾을 수 없음! chatRoomId={}", chatMessageDto.getChatRoomId());
			return;
		}

		// ✅ senderId 설정
		chatMessageDto.setSenderId(senderId);

		// ✅ chatRoomId와 senderId가 올바르게 전달되었는지 확인
		if (chatMessageDto.getChatRoomId() == null || chatMessageDto.getSenderId() == null) {
			log.error("❌ Received message with null values! chatRoomId={}, senderId={}",
				chatMessageDto.getChatRoomId(), chatMessageDto.getSenderId());
			return;
		}

		// ✅ 메시지를 RabbitMQ로 전송 (DTO 사용)
		chatMessageProducer.sendMessageToQueue(chatMessageDto);
	}
}

