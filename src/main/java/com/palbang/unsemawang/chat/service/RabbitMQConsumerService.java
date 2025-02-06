package com.palbang.unsemawang.chat.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.config.RabbitMQConfig;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;

@Service
public class RabbitMQConsumerService {

	private final ChatMessageService chatMessageService;

	@Autowired
	public RabbitMQConsumerService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	@RabbitListener(queues = RabbitMQConfig.CLIENT_TO_BACKEND_QUEUE)
	public void consumeMessageFromClient(String message) {
		System.out.println("Message received from RabbitMQ: " + message);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ChatMessageDto chatMessage = objectMapper.readValue(message, ChatMessageDto.class);

			// 메시지 처리
			chatMessageService.processIncomingMessage(chatMessage);
		} catch (Exception e) {
			System.err.println("Error processing message: " + e.getMessage());
			e.printStackTrace();
		}
	}
}