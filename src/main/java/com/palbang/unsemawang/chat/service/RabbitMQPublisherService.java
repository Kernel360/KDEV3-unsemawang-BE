package com.palbang.unsemawang.chat.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.chat.config.RabbitMQConfig;

@Service
public class RabbitMQPublisherService {

	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public RabbitMQPublisherService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publishToClient(String message) {
		rabbitTemplate.convertAndSend(
			RabbitMQConfig.EXCHANGE_NAME,         // Exchange
			RabbitMQConfig.BACKEND_TO_CLIENT_ROUTING_KEY, // Routing Key
			message
		);
		System.out.println("Message published to RabbitMQ for client: " + message);
	}
}