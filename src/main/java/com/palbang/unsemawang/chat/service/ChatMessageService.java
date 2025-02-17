package com.palbang.unsemawang.chat.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.activity.constant.ActiveStatus;
import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.repository.ActiveMemberRedisRepository;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.request.ChatMessageRequest;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
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
public class ChatMessageService {

	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final FileService fileService;
	private final ActiveMemberRedisRepository activeMemberRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String REDIS_CHAT_PREFIX = "chat:room:";

	public ChatMessageDto saveChatMessage(String memberId, Long chatRoomId, ChatMessageRequest chatMessageRequest) {
		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		Member sender = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "발신자를 찾을 수 없습니다. senderId=" + memberId));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
				"채팅방을 찾을 수 없습니다. chatRoomId=" + chatRoomId));

		if (chatRoom.getUser1() == null || chatRoom.getUser2() == null) {
			throw new GeneralException(ResponseCode.FORBIDDEN, "채팅방에서 메시지 입력이 차단되었습니다. 혼자 남은 상태입니다.");
		}

		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.content(chatMessageRequest.getMessage())
			.status(
				MessageStatus.RECEIVED)
			.timestamp(LocalDateTime.now())
			.build();

		String profileImageUrl = fileService.getProfileImgUrl(memberId);
		ChatMessageDto chatMessageDto = ChatMessageDto.builder()
			.chatRoomId(chatMessage.getChatRoom().getId())
			.senderId(chatMessage.getSender().getId())
			.content(chatMessage.getContent())
			.status(chatMessage.getStatus())
			// 로컬 데이트 타임을 밀리초(Long)로 변환 // 메세지큐 담을때 로컬데이트타임 담으면 에러난다고 합니다. (확인은 안해봤습니다.)
			.timestamp(chatMessage.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
			.profileImageUrl(profileImageUrl)
			.nickname(chatMessage.getSender().getNickname())
			.build();

		saveMessageToRedis(chatRoomId, chatMessageDto);

		return chatMessageDto;
	}

	public int getNotReadMessageOfPartner(String memberId, Long chatRoomId) {
		String partnerId = chatRoomRepository.findOtherMemberIdInChatRoom(chatRoomId, memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		return chatMessageRepository.countByChatRoomIdAndSenderIdNotAndStatus(
			chatRoomId, partnerId, MessageStatus.RECEIVED
		);
	}

	public String getReceiverId(String memberId, Long chatRoomId) {
		String receiverId = chatRoomRepository.findOtherMemberIdInChatRoom(chatRoomId, memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));
		return receiverId;
	}

	public ActiveMember getReceiverIdStatus(String memberId, Long chatRoomId) {
		//ActiveStatus status
		ActiveMember memberStatus = activeMemberRepository.findById(memberId).orElse(ActiveMember.builder().
			memberId(memberId).status(ActiveStatus.ACTIVE_CHATROOM).chatRoomId(chatRoomId).build());
		return memberStatus;
	}

	// Redis에서 현재 메시지가 저장된 모든 채팅방 키 조회
	public Set<String> getChatRoomKeysFromRedis() {
		return redisTemplate.keys(REDIS_CHAT_PREFIX + "*");
	}

	public void saveMessageToRedis(Long chatRoomId, ChatMessageDto message) {
		String redisKey = REDIS_CHAT_PREFIX + chatRoomId;
		redisTemplate.opsForList().rightPush(redisKey, message);
		log.info("Redis 저장 시도: Key={}, Message={}", redisKey, message);

		Long messageCount = redisTemplate.opsForList().size(redisKey);
		log.info("Redis 저장 완료: Key={}, MessageCount={}", redisKey, messageCount);

		if (messageCount != null && messageCount >= 100) {
			saveBatchMessagesToDB(chatRoomId);
		}
	}

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
		List<ChatMessageDto> messages = rawMessages.stream()
			.map(obj -> objectMapper.convertValue(obj, ChatMessageDto.class))
			.toList();

		// 채팅방 조회
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH, "채팅방을 찾을 수 없음"));

		// SenderId 목록 추출 후 Batch Fetch
		Set<String> senderIds = messages.stream()
			.map(ChatMessageDto::getSenderId)
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
			chatMessageRepository.saveAll(batch);
			log.info("{}~{}번째 메시지 저장 완료", i + 1, end);
		}

		redisTemplate.delete(redisKey);
		log.info("{}개의 메시지를 DB에 저장 완료 후 Redis에서 삭제: chatRoomId={}", chatMessages.size(), chatRoomId);
	}
}
