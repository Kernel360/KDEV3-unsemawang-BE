package com.palbang.unsemawang.chat.infrastructure;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.application.dto.ChatMessageRequestDto;
import com.palbang.unsemawang.chat.application.dto.ChatMessageResponseDto;
import com.palbang.unsemawang.chat.application.dto.NewChatMessageCountDto;
import com.palbang.unsemawang.chat.application.dto.NewChatMessageDto;
import com.palbang.unsemawang.chat.constant.MessageStatus;
import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.interfaces.ChatMessageReader;
import com.palbang.unsemawang.chat.domain.interfaces.ChatMessageStore;
import com.palbang.unsemawang.chat.domain.interfaces.ChatRoomReader;
import com.palbang.unsemawang.chat.domain.service.ChatMessageService;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageReader chatMessageReader;
	private final ChatMessageStore chatMessageStore;
	private final ChatMessageProducer chatMessageProducer;
	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, Object> redisTemplate;
	private final MemberRepository memberRepository;
	private final FileService fileService;

	private static final String REDIS_CHAT_PREFIX = "chat:room:";
	private final ChatRoomReader chatRoomReader;

	@Override
	@Transactional
	public void processMessage(Long chatRoomId, String senderId, ChatMessageRequestDto chatMessageRequestDto) {
		log.info("메시지 처리 시작: 채팅방 ID={}, 발신자={}, 메시지={}", chatRoomId, senderId, chatMessageRequestDto.getMessage());

		String destination = "/topic/chat/" + chatRoomId;

		Member sender = memberRepository.findById(senderId)
			.orElseThrow(
				() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "발신자를 찾을 수 없습니다. senderId=" + senderId));

		ChatMessageResponseDto chatMessage = ChatMessageResponseDto.builder()
			.chatRoomId(chatRoomId)
			.senderId(senderId)
			.content(chatMessageRequestDto.getMessage())
			.status(MessageStatus.RECEIVED)
			.timestamp(System.currentTimeMillis())
			.nickname(sender.getNickname())
			.profileImageUrl(fileService.getProfileImgUrl(senderId))
			.build();

		// WebSocket으로 메시지 전송
		messagingTemplate.convertAndSend(destination, chatMessage);
		log.info("WebSocket으로 메시지 전송: {}", chatMessage);

		// Redis에 메시지 저장
		saveMessageToRedis(chatRoomId, chatMessage);
	}

	// @Override
	// @Transactional
	// public void processMessage(Long chatRoomId, String senderId, ChatMessageRequestDto chatMessageRequestDto) {
	// 	log.info("메시지 처리 시작: 채팅방 ID={}, 발신자={}, 메시지={}", chatRoomId, senderId, chatMessageRequestDto.getMessage());
	//
	// 	String destination = "/topic/chat/" + chatRoomId;
	//
	// 	Member sender = memberRepository.findById(senderId)
	// 		.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
	// 			"발신자를 찾을 수 없습니다. senderId=" + senderId));
	//
	// 	ChatMessageInfo chatMessageInfo =
	// 		ChatMessageInfo.builder()
	// 			.chatRoomId(chatRoomId)
	// 			.senderId(senderId)
	// 			.content(chatMessageRequestDto.getMessage())
	// 			.status(MessageStatus.SENT)
	// 			.timestamp(System.currentTimeMillis())
	// 			.nickname(sender.getNickname())
	// 			.profileImageUrl(fileService.getProfileImgUrl(senderId))
	// 			.build();
	//
	// 	messagingTemplate.convertAndSend(destination, chatMessageInfo);
	// 	log.info("Forwarded WebSocket message: {}", chatMessageInfo);
	//
	// 	// RabbitMQ에 메시지 발행
	// 	chatMessageProducer.sendMessageToQueue(chatMessageInfo);
	// }

	@Override
	public void sendNewMessageNotification(Long chatRoomId, String senderId, String message) {

		String destination = "/topic/chat/" + chatRoomId + "/" + senderId + "/new-message";

		messagingTemplate.convertAndSend(destination,
			NewChatMessageDto.of(
				message,
				LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
			)
		);

		int unreadCount = getUnreadMessageCount(chatRoomId, senderId);
		sendUnreadMessageCount(unreadCount, destination);
	}

	@Override
	public int getUnreadMessageCount(Long chatRoomId, String senderId) {
		return chatMessageReader.countUnreadMessages(chatRoomId, senderId);
	}

	@Override
	public void sendUnreadMessageCount(int unreadCount, String destination) {
		messagingTemplate.convertAndSend(destination + "/count", NewChatMessageCountDto.of(unreadCount));
	}

	@Override
	@Transactional
	public void markMessagesAsRead(Long chatRoomId, String userId) {
		chatMessageStore.markMessagesAsRead(chatRoomId, userId);
	}

	// Redis에서 현재 메시지가 저장된 모든 채팅방 키 조회
	@Override
	public Set<String> getChatRoomKeysFromRedis() {
		return redisTemplate.keys(REDIS_CHAT_PREFIX + "*");
	}

	@Override
	public void saveMessageToRedis(Long chatRoomId, ChatMessageResponseDto message) {
		String redisKey = REDIS_CHAT_PREFIX + chatRoomId;
		redisTemplate.opsForList().rightPush(redisKey, message);
		log.info("Redis 저장 시도: Key={}, Message={}", redisKey, message);

		Long messageCount = redisTemplate.opsForList().size(redisKey);
		log.info("Redis 저장 완료: Key={}, MessageCount={}", redisKey, messageCount);

		if (messageCount != null && messageCount >= 100) {
			saveBatchMessagesToDB(chatRoomId);
		}
	}

	@Override
	public Long getMessageCountInRedis(Long chatRoomId) {
		String redisKey = REDIS_CHAT_PREFIX + chatRoomId;
		return redisTemplate.opsForList().size(redisKey);
	}

	@Override
	@Transactional
	public void saveBatchMessagesToDB(Long chatRoomId) {
		String redisKey = REDIS_CHAT_PREFIX + chatRoomId;
		List<Object> rawMessages = redisTemplate.opsForList().range(redisKey, 0, -1);

		if (rawMessages == null || rawMessages.isEmpty()) {
			log.info("Redis에 저장된 메시지가 없음: chatRoomId={}", chatRoomId);
			return;
		}

		// 직렬화된 JSON을 ChatMessageResponseDto로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		List<ChatMessageResponseDto> messages = rawMessages.stream()
			.map(obj -> objectMapper.convertValue(obj, ChatMessageResponseDto.class))
			.toList();

		// 채팅방 조회
		ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "채팅방을 찾을 수 없음"));

		// SenderId 목록 추출 후 Batch Fetch
		Set<String> senderIds = messages.stream()
			.map(ChatMessageResponseDto::getSenderId)
			.collect(Collectors.toSet());

		List<Member> members = memberRepository.findAllById(senderIds);
		Map<String, Member> memberMap = members.stream()
			.collect(Collectors.toMap(Member::getId, member -> member));

		List<ChatMessage> chatMessages = messages.stream()
			.map(dto -> {
				Member sender = memberMap.get(dto.getSenderId());
				if (sender == null) {
					log.warn("발신자 정보 없음: {}", dto.getSenderId());
					return null;
				}

				return ChatMessage.builder()
					.chatRoom(chatRoom)
					.sender(sender)
					.content(dto.getContent())
					.status(dto.getStatus())
					.timestamp(LocalDateTime.ofInstant(
						Instant.ofEpochMilli(dto.getTimestamp()),
						ZoneId.systemDefault()
					))
					.build();
			})
			.filter(Objects::nonNull)
			.toList();

		if (chatMessages.isEmpty()) {
			log.warn("변환된 메시지가 없음 (모두 변환 실패)");
			return;
		}

		// Batch Insert (100개씩 저장)
		final int batchSize = 100;
		for (int i = 0; i < chatMessages.size(); i += batchSize) {
			int end = Math.min(i + batchSize, chatMessages.size());
			List<ChatMessage> batch = chatMessages.subList(i, end);
			chatMessageStore.saveAll(batch);
			log.info("{}~{}번째 메시지 저장 완료", i + 1, end);
		}

		redisTemplate.delete(redisKey);
		log.info("{}개의 메시지를 DB에 저장 완료 후 Redis에서 삭제: chatRoomId={}", chatMessages.size(), chatRoomId);
	}
}
