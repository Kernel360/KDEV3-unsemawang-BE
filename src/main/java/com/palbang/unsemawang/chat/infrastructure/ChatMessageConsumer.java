// package com.palbang.unsemawang.chat2.infrastructure;
//
// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.palbang.unsemawang.chat2.application.dto.ChatMessageResponseDto;
// import com.palbang.unsemawang.chat2.constant.MessageStatus;
// import com.palbang.unsemawang.chat2.domain.service.ChatMessageService;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class ChatMessageConsumer {
//
// 	private final ChatMessageService chatMessageService;
// 	private final ObjectMapper objectMapper;
// 	private final SimpMessagingTemplate messagingTemplate;
//
// 	private static final int BATCH_THRESHOLD = 100;
//
// 	@RabbitListener(queues = "chat.queue")
// 	@Transactional
// 	public void consumeMessage(String messageJson) throws JsonProcessingException {
// 		log.info("RabbitMQ 메시지 소비: {}", messageJson);
//
// 		ChatMessageResponseDto message = objectMapper.readValue(messageJson, ChatMessageResponseDto.class);
//
// 		message = message.toBuilder()
// 			.status(MessageStatus.RECEIVED) // 상태 변경
// 			.build();
//
// 		Long chatRoomId = message.getChatRoomId();
//
// 		// Redis에 메시지 저장
// 		chatMessageService.saveMessageToRedis(chatRoomId, message);
//
// 		// 100개 이상이면 배치 저장 실행
// 		if (chatMessageService.getMessageCountInRedis(chatRoomId) >= BATCH_THRESHOLD) {
// 			chatMessageService.saveBatchMessagesToDB(chatRoomId);
// 		}
// 	}
// }
