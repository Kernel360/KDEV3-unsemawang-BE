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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		// 메시지가 Exchange까지 도달했는지 확인
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				log.info("메시지가 Exchange까지 정상 도착");
			} else {
				log.error("메시지 도착 실패: {}", cause);
			}
		});

		// 메시지가 Queue로 전달되지 못하면 반환하는 Callback
		rabbitTemplate.setReturnsCallback(returned -> {
			log.warn("메시지가 Queue로 전달되지 못함: {}", new String(returned.getMessage().getBody()));
			log.warn("Exchange: {}, Routing Key: {}", returned.getExchange(), returned.getRoutingKey());
		});

		return rabbitTemplate;
	}
}

