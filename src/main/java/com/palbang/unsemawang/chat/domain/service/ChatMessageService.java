package com.palbang.unsemawang.chat.domain.service;

import java.util.Set;

import com.palbang.unsemawang.chat.application.dto.ChatMessageRequestDto;
import com.palbang.unsemawang.chat.application.dto.ChatMessageResponseDto;

public interface ChatMessageService {

	void processMessage(Long chatRoomId, String senderId, ChatMessageRequestDto chatMessageRequestDto);

	void sendNewMessageNotification(Long chatRoomId, String senderId, String message);

	int getUnreadMessageCount(Long chatRoomId, String senderId);

	void sendUnreadMessageCount(int unreadCount, String destination);

	void markMessagesAsRead(Long chatRoomId, String userId);

	// Redis에서 현재 메시지가 저장된 모든 채팅방 키 조회
	Set<String> getChatRoomKeysFromRedis();

	void saveMessageToRedis(Long chatRoomId, ChatMessageResponseDto message);

	Long getMessageCountInRedis(Long chatRoomId);

	void saveBatchMessagesToDB(Long chatRoomId);
}

