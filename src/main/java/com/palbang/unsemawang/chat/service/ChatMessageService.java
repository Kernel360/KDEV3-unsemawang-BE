package com.palbang.unsemawang.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.MessageStatus;
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

	// 수신 메시지 처리 (DB 저장 및 RabbitMQ로 전송)
	public void processIncomingMessage(ChatMessageDto chatMessage) {
		// 메시지를 저장
		ChatMessage entity = new ChatMessage(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getType());
		chatMessageRepository.save(entity);

		// 필요 시, 추가 동작 수행
		rabbitMqPublisher.publishToClient("Message received: " + chatMessage.getContent());
	}

	// 메시지 상태 업데이트
	public void updateMessageStatus(Long messageId, MessageStatus status) {
		ChatMessage message = chatMessageRepository.findById(messageId).orElseThrow(() ->
			new IllegalArgumentException("Message not found with id: " + messageId));
		message.setStatus(status);
		chatMessageRepository.save(message);

		System.out.println("Updated message status to: " + status);
	}
}