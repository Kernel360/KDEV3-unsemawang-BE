package com.palbang.unsemawang.chat.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.dto.ChatRoomDto;
import com.palbang.unsemawang.chat.dto.response.ChatHistoryReadResponse;
import com.palbang.unsemawang.chat.dto.response.ChatMessageResponse;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chemistry.constant.FiveElements;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;

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

		return ChatRoomDto.fromEntity(chatRoom, null, targetUser, null);
	}

	/**
	 * 사용자의 채팅방 목록을 조회하며 마지막 메시지와 오행 정보를 포함
	 */
	@Transactional(readOnly = true)
	public List<ChatRoomDto> getChatRoomsWithLastMessage(String userId) {
		// 사용자가 참여한 모든 채팅방 조회
		List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);

		return chatRooms.stream().map(chatRoom -> {
			// 채팅방의 마지막 메시지 조회
			Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByChatRoomOrderByTimestampDesc(
				chatRoom);
			ChatMessage lastMessage = lastMessageOpt.orElse(null);

			// ✅ `receiverId`를 사용하여 상대방을 찾음
			String receiverId =
				chatRoom.getUser1().getId().equals(userId) ? chatRoom.getUser2().getId() : chatRoom.getUser1().getId();
			Member targetUser = memberRepository.findById(receiverId)
				.orElseThrow(() -> new IllegalStateException("❌ 대화 상대방을 찾을 수 없습니다. receiverId=" + receiverId));

			// 상대방의 오행 정보 가져오기
			String fiveElement = getUserFiveElement(targetUser.getId());

			return ChatRoomDto.fromEntity(chatRoom, lastMessage, targetUser, fiveElement);
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ChatHistoryReadResponse> getChatHistory(Long chatRoomId, String userId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없음"));

		// 사용자가 채팅방에 속해 있는지 확인
		if (!chatRoom.getUser1().getId().equals(userId) && !chatRoom.getUser2().getId().equals(userId)) {
			throw new IllegalArgumentException("채팅방에 속한 사용자가 아닙니다.");
		}

		// 채팅 내역 조회
		List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);

		// DTO 변환
		return chatMessages.stream()
			.map(message -> ChatHistoryReadResponse.builder()
				.partnerNickname(message.getSender().getNickname())
				.messages(List.of(ChatMessageResponse.builder()
					.content(message.getContent())
					.senderType(message.getSender().getId().equals(userId) ? SenderType.SELF : SenderType.OTHER)
					.senderId(message.getSender().getId())
					.senderNickname(message.getSender().getNickname())
					.senderProfileImageUrl(message.getSender().getProfileUrl())
					.timestamp(Instant.ofEpochMilli(message.getTimestamp())
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime()) // ✅ 변환
					.build()))
				.build())
			.collect(Collectors.toList());
	}

	@Transactional
	public void leaveChatRoom(String userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없음"));

		if (chatRoom.getUser1().getId().equals(userId)) {
			chatRoom.setUser1(null);
		} else if (chatRoom.getUser2().getId().equals(userId)) {
			chatRoom.setUser2(null);
		} else {
			throw new IllegalArgumentException("채팅방에 속한 사용자가 아님");
		}

		// 두 사용자가 모두 나갔으면 삭제
		if (chatRoom.getUser1() == null && chatRoom.getUser2() == null) {
			chatRoomRepository.delete(chatRoom);
		} else {
			chatRoomRepository.save(chatRoom);
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
