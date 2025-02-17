package com.palbang.unsemawang.chat.infrastructure.batch;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.palbang.unsemawang.chat.domain.service.ChatMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageBatchProcessor {

	private final ChatMessageService chatMessageService;

	private static final String REDIS_CHAT_PREFIX = "chat:room:";

	/**
	 * 1분마다 Redis에 저장된 메시지를 DB로 배치 저장
	 */
	@Scheduled(fixedRate = 60000) // 60초마다 실행
	public void processBatchMessages() {
		log.info("배치 메시지 저장 시작...");

		// Redis에서 모든 채팅방 키 조회
		Set<String> chatRoomKeys = chatMessageService.getChatRoomKeysFromRedis();
		if (chatRoomKeys.isEmpty()) {
			log.info("Redis에 저장된 배치 메시지가 없음.");
			return;
		}

		for (String redisKey : chatRoomKeys) {
			Long chatRoomId = Long.parseLong(redisKey.replace(REDIS_CHAT_PREFIX, ""));
			chatMessageService.saveBatchMessagesToDB(chatRoomId);
		}

		log.info("배치 메시지 저장 완료!");
	}
}
