package com.palbang.unsemawang.chat.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatMessageConsumer {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

	public ChatMessageConsumer(ChatMessageRepository chatMessageRepository,
		ChatRoomRepository chatRoomRepository,
		SimpMessagingTemplate messagingTemplate,
		ObjectMapper objectMapper) {
		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.messagingTemplate = messagingTemplate;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "chat.queue")
	public void consumeMessage(String messageJson) {
		try {
			ChatMessageDto chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto.class);

			// ✅ chatRoomId 확인 (senderId 검증 제거)
			if (chatMessageDto.getChatRoomId() == null) {
				log.error("❌ chatRoomId가 없는 메시지는 처리 불가! {}", chatMessageDto);
				return;
			}

			ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
				.orElseThrow(() -> new IllegalStateException(
					"❌ ChatRoom을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));

			ChatMessage chatMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.content(chatMessageDto.getContent())
				.status(MessageStatus.RECEIVED)
				.timestamp(
					chatMessageDto.getTimestamp() != null ? chatMessageDto.getTimestamp() : System.currentTimeMillis())
				.build();

			chatMessageRepository.save(chatMessage);
			log.info("✅ Chat message saved: {}", chatMessage);

			messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), chatMessageDto);
			log.info("📩 Forwarded WebSocket message: {}", chatMessageDto);

		} catch (Exception e) {
			log.error("❌ 메시지 처리 실패", e);
		}
	}
}

