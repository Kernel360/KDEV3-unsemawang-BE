package com.palbang.unsemawang.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final RabbitMQPublisherService rabbitMqPublisher;

	@Autowired
	public ChatMessageService(ChatMessageRepository chatMessageRepository, RabbitMQPublisherService rabbitMqPublisher) {
		this.chatMessageRepository = chatMessageRepository;
		this.rabbitMqPublisher = rabbitMqPublisher;
	}

	public void processIncomingMessage(ChatMessageDto chatMessage) {
		// 수신한 메시지를 처리 (DB 저장)
		ChatMessage entity = new ChatMessage(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getType());
		chatMessageRepository.save(entity);

		// 추가로, 필요하다면 메시지를 소켓 서버로 다시 전달
		rabbitMqPublisher.publishToClient("Message received: " + chatMessage.getContent());
	}
}
