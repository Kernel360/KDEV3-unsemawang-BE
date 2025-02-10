package com.palbang.unsemawang.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.ChatRoomDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chemistry.constant.FiveElements;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final FileService fileService;
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 사용자의 채팅방 목록을 조회
	 */
	@Transactional
	public ChatRoomDto createOrGetChatRoom(String senderId, String receiverId) {
		Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByUsers(senderId, receiverId);

		ChatRoom chatRoom = chatRoomOpt.orElseGet(() -> {
			Member sender = memberRepository.findById(senderId)
				.orElseThrow(() -> new IllegalArgumentException("발신자 정보를 찾을 수 없음"));
			Member receiver = memberRepository.findById(receiverId)
				.orElseThrow(() -> new IllegalArgumentException("수신자 정보를 찾을 수 없음"));

			ChatRoom newChatRoom = ChatRoom.createSortedChatRoom(sender, receiver);
			return chatRoomRepository.save(newChatRoom);
		});

		// ✅ receiverId를 기반으로 targetUser 찾기 (이제 NullPointerException 방지 가능)
		Member targetUser = memberRepository.findById(receiverId)
			.orElseThrow(() -> new IllegalStateException("❌ targetUser를 찾을 수 없습니다. receiverId=" + receiverId));

		// ✅ unreadCount(안 읽은 메시지 개수) 추가 (기본값: 0)
		return ChatRoomDto.fromEntity(chatRoom, null, targetUser, null, 0);
	}

	/**
	 * 사용자의 채팅방 목록을 조회하며 마지막 메시지와 오행 정보를 포함
	 */
	@Transactional(readOnly = true)
	public List<ChatRoomDto> getChatRoomsWithLastMessage(String userId) {
		List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);

		return chatRooms.stream().map(chatRoom -> {
			Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByChatRoomOrderByTimestampDesc(
				chatRoom);
			ChatMessage lastMessage = lastMessageOpt.orElse(null);

			Member targetUser = null;
			boolean isUser1 = chatRoom.getUser1() != null && !chatRoom.getUser1().getId().equals(userId);
			boolean isUser2 = chatRoom.getUser2() != null && !chatRoom.getUser2().getId().equals(userId);

			if (isUser1) {
				targetUser = chatRoom.getUser1();
			} else if (isUser2) {
				targetUser = chatRoom.getUser2();
			}

			int unreadCount = chatMessageRepository.countByChatRoomAndSenderIdNotAndStatus(chatRoom, userId,
				MessageStatus.RECEIVED);

			String fiveElement = (targetUser != null) ? getUserFiveElement(targetUser.getId()) : null;

			return ChatRoomDto.fromEntity(chatRoom, lastMessage, targetUser, fiveElement, unreadCount);
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ChatMessageDto> getChatHistory(Long chatRoomId, String userId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없음: " + chatRoomId));

		List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);

		if (chatMessages.isEmpty()) {
			log.warn("⚠️ 채팅 내역이 없습니다. chatRoomId={}", chatRoomId);
			return Collections.emptyList();
		}

		ObjectMapper objectMapper = new ObjectMapper();

		List<ChatMessageDto> chatMessageDtos = chatMessages.stream()
			.map(message -> {
				if (message == null || message.getSender() == null) {
					log.warn("⚠️ sender가 NULL인 메시지가 있음, messageId={}", message != null ? message.getId() : "Unknown");
					return null;
				}

				// ✅ Lazy Loading 문제 해결 (sender 강제 초기화)
				Hibernate.initialize(message.getSender());

				SenderType senderType = message.getSender().getId().equals(userId) ? SenderType.SELF : SenderType.OTHER;

				// ✅ 프로필 이미지 URL 가져오기
				String profileImageUrl = fileService.getProfileImgUrl(message.getSender().getId());

				if (profileImageUrl == null || profileImageUrl.isEmpty()) {
					profileImageUrl = "https://cdn.example.com/default-profile.png"; // 기본 프로필 이미지 URL
				}

				ChatMessageDto dto = ChatMessageDto.builder()
					.chatRoomId(chatRoom.getId())
					.senderId(message.getSender().getId())
					.nickname(message.getSender().getNickname() != null ? message.getSender().getNickname() : "Unknown")
					.profileImageUrl(profileImageUrl) // ✅ 프로필 이미지 추가
					.content(message.getContent())
					.timestamp(message.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
					.status(message.getStatus())
					.senderType(senderType)
					.build();

				try {
					String jsonOutput = objectMapper.writeValueAsString(dto);
					log.info("📩 직렬화된 메시지 JSON: {}", jsonOutput);
				} catch (Exception e) {
					log.error("❌ JSON 직렬화 오류 발생", e);
				}

				return dto;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		return chatMessageDtos;
	}

	@Transactional
	public void leaveChatRoom(String userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new IllegalArgumentException("❌ 채팅방을 찾을 수 없음: " + chatRoomId));

		// ✅ 현재 사용자 제거
		boolean isUser1 = chatRoom.getUser1() != null && chatRoom.getUser1().getId().equals(userId);
		boolean isUser2 = chatRoom.getUser2() != null && chatRoom.getUser2().getId().equals(userId);

		if (isUser1) {
			chatRoom.setUser1(null);
		} else if (isUser2) {
			chatRoom.setUser2(null);
		} else {
			throw new IllegalArgumentException("❌ 채팅방에 속한 사용자가 아님");
		}

		// ✅ 남아있는 사용자가 있는지 확인
		Member targetUser = isUser1 ? chatRoom.getUser2() : chatRoom.getUser1();

		if (targetUser != null) {
			// ✅ 남아있는 사용자에게 "상대방이 나갔습니다." 메시지 전송
			ChatMessage leaveMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.sender(null) // 시스템 메시지
				.content("상대방이 채팅방을 나갔습니다. 메시지를 보낼 수 없습니다.")
				.status(MessageStatus.RECEIVED)
				.timestamp(LocalDateTime.now())
				.build();

			chatMessageRepository.save(leaveMessage);

			messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId,
				ChatMessageDto.builder()
					.chatRoomId(chatRoomId)
					.content("상대방이 채팅방을 나갔습니다. 메시지를 보낼 수 없습니다.")
					.status(MessageStatus.RECEIVED)
					.timestamp(System.currentTimeMillis())
					.nickname("시스템")
					.senderId(null)
					.senderType(SenderType.OTHER)
					.build()
			);
		}

		// ✅ 채팅방이 완전히 비었으면 삭제 처리
		if (chatRoom.getUser1() == null && chatRoom.getUser2() == null) {
			chatRoom.setDelete(true);
		}

		chatRoomRepository.save(chatRoom);
	}

	/**
	 * 채팅방의 안 읽은 메시지를 READ 상태로 업데이트
	 */
	@Transactional
	public void markMessagesAsRead(Long chatRoomId, String userId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new IllegalArgumentException("❌ 채팅방을 찾을 수 없음: " + chatRoomId));

		// ✅ 상대방이 보낸 안 읽은 메시지 가져오기
		List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoomAndSenderIdNotAndStatus(
			chatRoom, userId, MessageStatus.RECEIVED);  // ❗ 변경된 쿼리 적용

		if (!unreadMessages.isEmpty()) {
			unreadMessages.forEach(message -> message.setStatus(MessageStatus.READ));
			chatMessageRepository.saveAll(unreadMessages);
		}
	}

	/**
	 * 사용자 오행 정보 조회
	 */
	private String getUserFiveElement(String userId) {
		return fortuneUserInfoRepository.findByMemberIdRelationIdIsOne(userId)
			.map(info -> FiveElements.convertToChinese(info.getDayGan()))
			.orElse(null);
	}
}
