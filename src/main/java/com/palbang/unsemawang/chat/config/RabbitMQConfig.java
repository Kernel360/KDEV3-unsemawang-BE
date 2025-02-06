package com.palbang.unsemawang.chat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "chat.exchange";
	public static final String CLIENT_TO_BACKEND_QUEUE = "client.to.backend.queue";
	public static final String BACKEND_TO_CLIENT_QUEUE = "backend.to.client.queue";

	public static final String CLIENT_TO_BACKEND_ROUTING_KEY = "client.to.backend.key";
	public static final String BACKEND_TO_CLIENT_ROUTING_KEY = "backend.to.client.key";

	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue clientToBackendQueue() {
		return QueueBuilder.durable(CLIENT_TO_BACKEND_QUEUE).build();
	}

	@Bean
	public Queue backendToClientQueue() {
		return QueueBuilder.durable(BACKEND_TO_CLIENT_QUEUE).build();
	}

	@Bean
	public Binding clientToBackendBinding() {
		return BindingBuilder
			.bind(clientToBackendQueue())
			.to(chatExchange())
			.with(CLIENT_TO_BACKEND_ROUTING_KEY);
	}

	@Bean
	public Binding backendToClientBinding() {
		return BindingBuilder
			.bind(backendToClientQueue())
			.to(chatExchange())
			.with(BACKEND_TO_CLIENT_ROUTING_KEY);
	}
}