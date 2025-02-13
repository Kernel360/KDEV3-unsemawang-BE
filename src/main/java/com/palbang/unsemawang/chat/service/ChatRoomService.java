package com.palbang.unsemawang.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.ChatRoomDto;
import com.palbang.unsemawang.chat.dto.response.ChatHistoryReadResponse;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chemistry.constant.FiveElements;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
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
	 * 사용자의 채팅방 목록을 조회하거나 새로 생성
	 */
	@Transactional
	public ChatRoomDto createOrGetChatRoom(String senderId, String receiverId) {
		ChatRoom chatRoom = chatRoomRepository.findByUsers(senderId, receiverId)
			.orElseGet(() -> {
				Member sender = memberRepository.findById(senderId)
					.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));
				Member receiver = memberRepository.findById(receiverId)
					.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

				ChatRoom newChatRoom = ChatRoom.createSortedChatRoom(sender, receiver);
				return chatRoomRepository.save(newChatRoom);
			});

		Member targetUser = memberRepository.findById(receiverId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		String profileImageUrl = fileService.getProfileImgUrl(targetUser.getId());

		return ChatRoomDto.fromEntity(chatRoom, null, targetUser, null, 0, profileImageUrl);
	}

	/**
	 * 사용자의 채팅방 목록을 조회하며 마지막 메시지와 오행 정보를 포함
	 */
	@Transactional(readOnly = true)
	public List<ChatRoomDto> getChatRoomsWithLastMessage(String userId) {
		List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);

		return chatRooms.stream()
			.filter(chatRoom -> {
				// 나간 사용자는 목록에서 제외
				if (chatRoom.getUser1().getId().equals(userId) && chatRoom.isUser1Out()) {
					return false;
				}
				if (chatRoom.getUser2().getId().equals(userId) && chatRoom.isUser2Out()) {
					return false;
				}
				return true;
			})
			.map(chatRoom -> {
				ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomOrderByTimestampDesc(chatRoom)
					.orElse(null);

				Member targetUser = getTargetUser(chatRoom, userId)
					.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

				String profileImageUrl = fileService.getProfileImgUrl(targetUser.getId());

				int unreadCount = chatMessageRepository.countByChatRoomAndSenderIdNotAndStatus(
					chatRoom, userId, MessageStatus.RECEIVED);

				String fiveElement = getUserFiveElement(targetUser.getId());

				return ChatRoomDto.fromEntity(chatRoom, lastMessage, targetUser, fiveElement, unreadCount,
					profileImageUrl);
			}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ChatHistoryReadResponse getChatHistory(Long chatRoomId, String userId) {
		// 채팅방 및 메시지 검색
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);

		// 메세지가 없을 경우 빈 리스트 반환
		if (chatMessages.isEmpty()) {
			return ChatHistoryReadResponse.builder()
				.messages(Collections.emptyList())
				.partnerNickname(null)
				.partnerId(null)
				.build();
		}

		// 채팅 상대방 ID 가져오기
		String partnerId = chatRoomRepository.findOtherMemberIdInChatRoom(chatRoomId, userId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 상대방 사용자 정보 조회
		Member partner = memberRepository.findById(partnerId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		String partnerNickname = Optional.ofNullable(partner.getNickname()).orElse("Unknown");

		// 메시지 DTO 변환
		List<ChatMessageDto> messageDtos = chatMessages.stream()
			.filter(Objects::nonNull)
			.map(message -> {
				SenderType senderType = message.getSender().getId().equals(userId) ? SenderType.SELF : SenderType.OTHER;

				String profileImageUrl = fileService.getProfileImgUrl(message.getSender().getId());

				return ChatMessageDto.builder()
					.chatRoomId(chatRoom.getId())
					.senderId(message.getSender().getId())
					.nickname(Optional.ofNullable(message.getSender().getNickname()).orElse("Unknown"))
					.profileImageUrl(profileImageUrl)
					.content(message.getContent())
					.timestamp(message.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
					.status(message.getStatus())
					//.senderType(senderType)
					.build();
			})
			.collect(Collectors.toList());

		// 응답 객체 생성
		return ChatHistoryReadResponse.builder()
			.partnerNickname(partnerNickname)
			.partnerId(partnerId)
			.messages(messageDtos)
			.build();
	}

	/**
	 * 사용자가 채팅방을 떠남
	 */
	@Transactional
	public void leaveChatRoom(String userId, Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		if (chatRoom.getUser1().getId().equals(userId)) {
			chatRoom.setUser1Out(true);
		} else if (chatRoom.getUser2().getId().equals(userId)) {
			chatRoom.setUser2Out(true);
		} else {
			throw new GeneralException(ResponseCode.FORBIDDEN, "사용자가 채팅방의 유효한 구성원이 아닙니다.");
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
			ChatMessageDto.builder()
				.chatRoomId(chatRoomId)
				.content("상대방이 채팅방을 나갔습니다. 메시지를 보낼 수 없습니다.")
				.status(MessageStatus.SYSTEM)
				.timestamp(System.currentTimeMillis())
				.nickname("시스템")
				.senderId(null)
				//.senderType(SenderType.OTHER)
				.build()
		);

		chatRoomRepository.save(chatRoom);
	}

	/**
	 * 채팅방의 안 읽은 메시지를 READ 상태로 업데이트
	 */
	@Transactional
	public void markMessagesAsRead(Long chatRoomId, String userId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoomAndSenderIdNotAndStatus(
			chatRoom, userId, MessageStatus.RECEIVED);

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

	/**
	 * 채팅방에서 사용자를 제거하는 메서드
	 */
	private boolean removeUserFromChatRoom(ChatRoom chatRoom, String userId, boolean isUser1) {
		if (isUser1 && chatRoom.getUser1() != null && chatRoom.getUser1().getId().equals(userId)) {
			chatRoom.setUser1(null);
			return true;
		} else if (!isUser1 && chatRoom.getUser2() != null && chatRoom.getUser2().getId().equals(userId)) {
			chatRoom.setUser2(null);
			return true;
		}
		return false;
	}

	/**
	 * 채팅방에서 대상 사용자를 찾는 메서드
	 */
	private Optional<Member> getTargetUser(ChatRoom chatRoom, String userId) {
		if (chatRoom.getUser1() != null && !chatRoom.getUser1().getId().equals(userId)) {
			return Optional.of(chatRoom.getUser1());
		} else if (chatRoom.getUser2() != null && !chatRoom.getUser2().getId().equals(userId)) {
			return Optional.of(chatRoom.getUser2());
		}
		return Optional.empty();
	}
}
