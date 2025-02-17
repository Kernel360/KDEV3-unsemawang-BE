package com.palbang.unsemawang.chat.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.chat.application.dto.ChatHistoryReadResponse;
import com.palbang.unsemawang.chat.application.dto.ChatMessageRequestDto;
import com.palbang.unsemawang.chat.application.dto.ChatMessageResponseDto;
import com.palbang.unsemawang.chat.application.dto.ChatRoomResponseDto;
import com.palbang.unsemawang.chat.domain.info.ChatHistoryReadInfo;
import com.palbang.unsemawang.chat.domain.info.ChatMessageInfo;
import com.palbang.unsemawang.chat.domain.info.ChatRoomInfo;
import com.palbang.unsemawang.chat.domain.service.ChatMessageService;
import com.palbang.unsemawang.chat.domain.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatFacade {

	private final ChatRoomService chatRoomService;
	private final ChatMessageService chatMessageService;

	/**
	 * WebSocket 메시지를 수신하고 RabbitMQ를 통해 비동기 처리
	 */
	public void processMessage(Long chatRoomId, String senderId, ChatMessageRequestDto chatMessageRequestDto) {
		// 1.RabbitMQ를 통해 메시지를 발행 (비동기)
		chatMessageService.processMessage(chatRoomId, senderId, chatMessageRequestDto);

		// 2. 새로운 메시지 알림 전송
		sendNewMessageNotification(chatRoomId, senderId, chatMessageRequestDto.getMessage());
	}

	/**
	 * 새로운 메시지 알림 전송 (WebSocket을 통해 클라이언트에 전송)
	 */
	private void sendNewMessageNotification(Long chatRoomId, String senderId, String message) {
		// 새로운 메시지 DTO 생성 및 전송
		chatMessageService.sendNewMessageNotification(chatRoomId, senderId, message);

		log.info("새로운 메시지 알림 전송 완료: 채팅방 ID={}, 발신자={}", chatRoomId, senderId);
	}

	/**
	 * 채팅방 입장 (채팅 내역 조회 + 안 읽은 메시지 읽음 처리)
	 */
	public ChatHistoryReadResponse enterChatRoom(String userId, Long chatRoomId) {
		log.info("채팅방 입장: userId={}, chatRoomId={}", userId, chatRoomId);

		// 1.안 읽은 메시지 읽음 처리
		chatMessageService.markMessagesAsRead(chatRoomId, userId);

		// 2.채팅 내역 조회
		ChatHistoryReadInfo chatHistory = chatRoomService.enterChatRoom(userId, chatRoomId);

		return convertToHistory(chatHistory);
	}

	/**
	 * 채팅방 생성 또는 기존 채팅방 반환
	 */
	public ChatRoomResponseDto createChatRoom(String userId, String partnerId) {
		log.info("채팅방 생성 요청: userId={}, partnerId={}", userId, partnerId);

		return convertToResponseDto(chatRoomService.createChatRoom(userId, partnerId));
	}

	/**
	 * 사용자의 채팅방 목록 조회
	 */
	public List<ChatRoomResponseDto> getUserChatRooms(String userId) {
		log.info("채팅방 목록 조회: userId={}", userId);
		List<ChatRoomInfo> chatRoomInfo = chatRoomService.getUserChatRooms(userId);

		return chatRoomInfo.stream()
			.map(this::convertToResponseDto)
			.toList();
	}

	/**
	 * 채팅방 나가기
	 */
	public void leaveChatRoom(String userId, Long chatRoomId) {
		log.info("채팅방 나가기: userId={}, chatRoomId={}", userId, chatRoomId);
		chatRoomService.leaveChatRoom(userId, chatRoomId);
	}

	/**
	 * Info -> ResponseDto 컨버터
	 */
	private ChatRoomResponseDto convertToResponseDto(ChatRoomInfo chatRoomInfo) {
		return ChatRoomResponseDto.builder()
			.chatRoomId(chatRoomInfo.getChatRoomId())
			.userId(chatRoomInfo.getUserId())
			.nickname(chatRoomInfo.getNickname())
			.profileImageUrl(chatRoomInfo.getProfileImageUrl())
			.lastChat(chatRoomInfo.getLastChat())
			.lastChatTime(chatRoomInfo.getLastChatTime())
			.sex(chatRoomInfo.getSex())
			.five(chatRoomInfo.getFive())
			.unreadCount(chatRoomInfo.getUnreadCount())
			.build();
	}

	private ChatHistoryReadResponse convertToHistory(ChatHistoryReadInfo chatHistoryReadInfo) {
		return ChatHistoryReadResponse.builder()
			.partnerNickname(chatHistoryReadInfo.getPartnerNickname())
			.partnerId(chatHistoryReadInfo.getPartnerId())
			.messages(chatHistoryReadInfo.getMessages().stream()
				.map(this::convertMessageInfoToResponse)
				.toList())
			.build();
	}

	private ChatMessageResponseDto convertMessageInfoToResponse(ChatMessageInfo chatMessageInfo) {
		return ChatMessageResponseDto.builder()
			.chatRoomId(chatMessageInfo.getChatRoomId())
			.senderId(chatMessageInfo.getSenderId())
			.content(chatMessageInfo.getContent())
			.status(chatMessageInfo.getStatus())
			.timestamp(chatMessageInfo.getTimestamp())
			.nickname(chatMessageInfo.getNickname())
			.profileImageUrl(chatMessageInfo.getProfileImageUrl())
			.build();
	}
}
