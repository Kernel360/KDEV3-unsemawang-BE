package com.palbang.unsemawang.chat.infrastructure;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashSet;
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
import com.palbang.unsemawang.chat.application.dto.ChatMessageResponseDto;
import com.palbang.unsemawang.chat.constant.MessageStatus;
import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.info.ChatHistoryReadInfo;
import com.palbang.unsemawang.chat.domain.info.ChatMessageInfo;
import com.palbang.unsemawang.chat.domain.info.ChatRoomInfo;
import com.palbang.unsemawang.chat.domain.interfaces.ChatMessageReader;
import com.palbang.unsemawang.chat.domain.interfaces.ChatRoomReader;
import com.palbang.unsemawang.chat.domain.interfaces.ChatRoomStore;
import com.palbang.unsemawang.chat.domain.repository.ChatMessageRepositoryCustom;
import com.palbang.unsemawang.chat.domain.service.ChatRoomService;
import com.palbang.unsemawang.chemistry.constant.FiveElements;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

	private final MemberRepository memberRepository;
	private final ChatRoomReader chatRoomReader;
	private final ChatRoomStore chatRoomStore;
	private final ChatMessageReader chatMessageReader;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final FileService fileService;
	private final ChatMessageRepositoryCustom chatMessageRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String REDIS_CHAT_PREFIX = "chat:room:";

	@Override
	@Transactional
	public ChatRoomInfo createChatRoom(String userId, String partnerId) {
		log.info("🛠 채팅방 생성 요청: userId={}, partnerId={}", userId, partnerId);

		Member user = memberRepository.findById(userId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));
		Member partner = memberRepository.findById(partnerId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		String profileImageUrl = fileService.getProfileImgUrl(userId);

		ChatRoom chatRoom = chatRoomStore.save(ChatRoom.createSortedChatRoom(user, partner));

		FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findByMemberIdRelationIdIsOne(partnerId)
			.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH));

		char sex = fortuneUserInfo.getSex();

		String fiveElement = getUserFiveElement(partnerId);

		ChatRoomInfo chatRoomInfo = ChatRoomInfo.fromEntity(chatRoom, null, partner, fiveElement, sex, 0,
			profileImageUrl);

		return chatRoomInfo;
	}

	@Override
	public List<ChatRoomInfo> getUserChatRooms(String userId) {

		List<ChatRoom> chatRooms = chatRoomReader.findByUserId(userId);

		if (chatRooms.isEmpty()) {
			return List.of(); // 채팅방이 없으면 바로 반환
		}

		Map<Long, ChatMessage> lastMessages = chatMessageReader.findLatestMessagesForChatRooms(
			chatRooms.stream().map(ChatRoom::getId).collect(Collectors.toSet())
		);

		Map<Long, String> chatRoomPartnerIds = chatRoomReader.findOtherMemberIdsInChatRooms(
			chatRooms.stream().map(ChatRoom::getId).collect(Collectors.toSet()), userId
		);

		Set<String> partnerIds = new HashSet<>(chatRoomPartnerIds.values());
		Map<String, Member> partnerMap = memberRepository.findAllById(partnerIds)
			.stream().collect(Collectors.toMap(Member::getId, member -> member));

		Map<String, FortuneUserInfo> fortuneMap = fortuneUserInfoRepository.findByMemberIds(partnerIds)
			.stream().collect(Collectors.toMap(f -> f.getMember().getId(), f -> f));

		return chatRooms.stream()
			.filter(chatRoom -> !(chatRoom.getUser1().getId().equals(userId) && chatRoom.isUser1Out())
				&& !(chatRoom.getUser2().getId().equals(userId) && chatRoom.isUser2Out()))
			.map(chatRoom -> {
				String partnerId = chatRoomPartnerIds.get(chatRoom.getId());
				Member partner = partnerMap.get(partnerId);
				FortuneUserInfo fortuneUserInfo = fortuneMap.get(partnerId);

				if (partner == null) {
					log.warn("상대방 정보를 찾을 수 없음: chatRoomId={}, partnerId={}", chatRoom.getId(), partnerId);
					return null; // null 반환을 방지하려면 예외 처리 가능
				}

				String profileImageUrl = fileService.getProfileImgUrl(partnerId);
				char sex = (fortuneUserInfo != null) ? fortuneUserInfo.getSex() : 'N';
				String fiveElement = (fortuneUserInfo != null) ? getUserFiveElement(partnerId) : null;
				ChatMessage lastMessage = lastMessages.get(chatRoom.getId());
				int unreadCount = chatMessageReader.countUnreadMessages(chatRoom.getId(), partnerId);

				return ChatRoomInfo.fromEntity(chatRoom, lastMessage, partner, fiveElement, sex, unreadCount,
					profileImageUrl);
			})
			.filter(Objects::nonNull) // null 제거
			.sorted(Comparator.comparing(ChatRoomInfo::getLastChatTime).reversed()) // 최신순 정렬
			.toList();
	}

	@Override
	public ChatRoomInfo getChatRoomInfo(Long chatRoomId) {
		ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
			.orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
		return ChatRoomInfo.fromEntity(chatRoom, null, null, null, 'N', 0, null);
	}

	@Override
	public ChatHistoryReadInfo enterChatRoom(String userId, Long chatRoomId) {
		log.info("채팅방 입장: userId={}, chatRoomId={}", userId, chatRoomId);

		// 상대방 ID 조회
		String partnerId = chatRoomReader.findOtherMemberIdInChatRoom(chatRoomId, userId);

		// 상대방 정보 조회 (탈퇴한 경우 예외 처리)
		Member partner = memberRepository.findById(partnerId).orElse(null);
		String partnerNickname = (partner != null) ? partner.getNickname() : "탈퇴한 사용자";
		String profileImageUrl = (partner != null) ? fileService.getProfileImgUrl(partnerId) : null;

		// 1. Redis에서 메시지 조회
		String redisKey = REDIS_CHAT_PREFIX + chatRoomId;
		List<Object> rawMessages = redisTemplate.opsForList().range(redisKey, 0, -1);

		// ObjectMapper 사용하여 ChatMessageResponseDto로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		List<ChatMessageInfo> redisMessages = (rawMessages != null) ? rawMessages.stream()
			.map(obj -> {
				try {
					ChatMessageResponseDto dto = objectMapper.convertValue(obj, ChatMessageResponseDto.class);
					return ChatMessageInfo.builder()
						.chatRoomId(chatRoomId)
						.senderId(dto.getSenderId())
						.nickname(dto.getNickname())
						.profileImageUrl(dto.getProfileImageUrl())
						.content(dto.getContent())
						.status(dto.getStatus())
						.timestamp(dto.getTimestamp())
						.build();
				} catch (Exception e) {
					log.error("Redis 메시지 변환 오류: {}", obj, e);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.toList() : List.of();

		// 2. DB에서 메시지 조회
		List<ChatMessage> dbMessages = chatMessageReader.findByChatRoomId(chatRoomId);
		List<ChatMessageInfo> dbMessageInfos = dbMessages.stream()
			.map(message -> {
				boolean isSystemMessage = (message.getSender() == null);
				return ChatMessageInfo.builder()
					.chatRoomId(chatRoomId)
					.senderId(isSystemMessage ? null : message.getSender().getId())
					.nickname(isSystemMessage ? "시스템" : message.getSender().getNickname())
					.profileImageUrl(isSystemMessage ? null : fileService.getProfileImgUrl(message.getSender().getId()))
					.content(message.getContent())
					.status(message.getStatus())
					.timestamp(message.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
					.build();
			})
			.toList();

		// 3. Redis + DB 메시지 합쳐서 시간순 정렬
		List<ChatMessageInfo> allMessages = redisMessages.stream()
			.filter(
				msg -> dbMessageInfos.stream().noneMatch(dbMsg -> dbMsg.getTimestamp() == msg.getTimestamp())) // 중복 제거
			.collect(Collectors.toList());

		allMessages.addAll(dbMessageInfos);

		allMessages = allMessages.stream()
			.sorted(Comparator.comparing(ChatMessageInfo::getTimestamp)) // 시간순 정렬
			.toList();

		return ChatHistoryReadInfo.builder()
			.partnerNickname(partnerNickname)
			.partnerId(partnerId)
			.messages(allMessages)
			.build();
	}

	@Override
	@Transactional
	public void leaveChatRoom(String userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
			.orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

		if (chatRoom.getUser1().getId().equals(userId)) {
			chatRoom.setUser1Out(true);
		} else if (chatRoom.getUser2().getId().equals(userId)) {
			chatRoom.setUser2Out(true);
		}

		ChatMessage leaveMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(null)
			.content("상대방이 채팅방을 나갔습니다. 메시지를 보낼 수 없습니다.")
			.status(MessageStatus.SYSTEM)
			.timestamp(LocalDateTime.now())
			.build();

		chatMessageRepository.save(leaveMessage);

		messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId,
			ChatMessageInfo.builder()
				.chatRoomId(chatRoomId)
				.content("상대방이 채팅방을 나갔습니다. 메시지를 보낼 수 없습니다.")
				.status(MessageStatus.SYSTEM)
				.timestamp(System.currentTimeMillis())
				.nickname("시스템")
				.senderId(null)
				//.senderType(SenderType.OTHER)
				.build()
		);

		chatRoomStore.save(chatRoom);
		log.info("채팅방 나가기 완료: userId={}, chatRoomId={}", userId, chatRoomId);
	}

	private String getUserFiveElement(String userId) {
		return fortuneUserInfoRepository.findByMemberIdRelationIdIsOne(userId)
			.map(info -> FiveElements.convertToChinese(info.getDayGan()))
			.orElse(null);
	}
}
