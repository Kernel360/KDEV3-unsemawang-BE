package com.palbang.unsemawang.chat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	// Exchange 이름
	public static final String EXCHANGE_NAME = "chat.exchange";
	// 큐 이름
	public static final String CLIENT_TO_BACKEND_QUEUE = "client.to.backend.queue";
	public static final String BACKEND_TO_CLIENT_QUEUE = "backend.to.client.queue";
	// 라우팅 키
	public static final String CLIENT_TO_BACKEND_ROUTING_KEY = "client.to.backend.key";
	public static final String BACKEND_TO_CLIENT_ROUTING_KEY = "backend.to.client.key";

	// Dead Letter Queue 관련 설정
	public static final String DLQ_NAME = "dead.letter.queue";
	public static final String RETRY_QUEUE_NAME = "retry.queue";
	public static final String RETRY_EXCHANGE_NAME = "retry.exchange";

	// 메인 Exchange 생성
	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	// DLQ용 Retry Exchange 생성
	@Bean
	public TopicExchange retryExchange() {
		return new TopicExchange(RETRY_EXCHANGE_NAME);
	}

	// 클라이언트 → 백엔드 큐: DLQ 관련 인자 포함
	@Bean
	public Queue clientToBackendQueue() {
		return QueueBuilder
			.durable(CLIENT_TO_BACKEND_QUEUE)
			.withArgument("x-dead-letter-exchange", RETRY_EXCHANGE_NAME) // 실패 시 이동할 Exchange
			.withArgument("x-dead-letter-routing-key", DLQ_NAME)          // DLQ로 메시지 이동
			.build();
	}

	// 백엔드 → 클라이언트 큐: 단순 큐
	@Bean
	public Queue backendToClientQueue() {
		return QueueBuilder.durable(BACKEND_TO_CLIENT_QUEUE).build();
	}

	// Dead Letter Queue (DLQ)
	@Bean
	public Queue deadLetterQueue() {
		return QueueBuilder.durable(DLQ_NAME).build();
	}

	// Retry 큐: 일정 시간 후 메인 Exchange로 메시지 전달
	@Bean
	public Queue retryQueue() {
		return QueueBuilder
			.durable(RETRY_QUEUE_NAME)
			.withArgument("x-dead-letter-exchange", EXCHANGE_NAME)         // 재시도 후 메인 Exchange로
			.withArgument("x-dead-letter-routing-key", CLIENT_TO_BACKEND_QUEUE) // 원래 큐로 돌아감
			.withArgument("x-message-ttl", 5000) // 메시지 TTL (예: 5000ms)
			.build();
	}

	// 바인딩 설정: 클라이언트 → 백엔드 큐
	@Bean
	public Binding clientToBackendBinding() {
		return BindingBuilder
			.bind(clientToBackendQueue())
			.to(chatExchange())
			.with(CLIENT_TO_BACKEND_ROUTING_KEY);
	}

	// 바인딩 설정: 백엔드 → 클라이언트 큐
	@Bean
	public Binding backendToClientBinding() {
		return BindingBuilder
			.bind(backendToClientQueue())
			.to(chatExchange())
			.with(BACKEND_TO_CLIENT_ROUTING_KEY);
	}

	// DLQ 바인딩: DLQ를 Retry Exchange에 바인딩
	@Bean
	public Binding dlqBinding() {
		return BindingBuilder
			.bind(deadLetterQueue())
			.to(retryExchange())
			.with(DLQ_NAME);
	}

	// Retry 큐 바인딩
	@Bean
	public Binding retryBinding() {
		return BindingBuilder
			.bind(retryQueue())
			.to(retryExchange())
			.with(RETRY_QUEUE_NAME);
	}

	// RabbitTemplate 설정 (메인 Exchange 사용)
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setExchange(EXCHANGE_NAME);
		return template;
	}
}
