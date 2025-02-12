package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chat.service.ChatMessageProducer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@Slf4j
@Controller
@AllArgsConstructor
public class ChatController {

	private final ChatMessageProducer chatMessageProducer;
	private final ChatRoomRepository chatRoomRepository;

	@Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 메시지를 전송하고 RabbitMQ로 전달합니다.")
	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
		log.info("📩 Received WebSocket message: {}", chatMessageDto);

		if (chatMessageDto.getChatRoomId() == null) {
			log.error("chatRoomId가 누락됨! {}", chatMessageDto);
			return;
		}

		ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
			.orElseThrow(
				() -> new IllegalStateException("채팅방을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));

		boolean isReadOnly = (chatRoom.getUser1() == null || chatRoom.getUser2() == null);
		if (isReadOnly) {
			log.warn("채팅방 {} 에서 메시지 입력 차단됨 - 혼자 남은 상태", chatRoom.getId());
			return;
		}

		try {
			log.info("Sending message to RabbitMQ: {}", chatMessageDto);
			chatMessageProducer.sendMessageToQueue(chatMessageDto);
			log.info("Message sent to RabbitMQ successfully!");
		} catch (Exception e) {
			log.error("Failed to send message to RabbitMQ", e);
		}
	}
}
