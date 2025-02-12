package com.palbang.unsemawang.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange("chat.exchange");
	}

	@Bean
	public Queue chatQueue() {
		return new Queue("chat.queue", true);
	}

	@Bean
	public Binding bindingChatQueue(Queue chatQueue, TopicExchange chatExchange) {
		return BindingBuilder.bind(chatQueue).to(chatExchange).with("chat.routing.key");
	}

	// JSON 직렬화 메시지 컨버터 설정
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// RabbitTemplate에 JSON 컨버터 적용
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}

