package com.palbang.unsemawang.chat.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.palbang.unsemawang.chat.constant.MessageStatus;
import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.entity.ChatRoom;

public interface ChatMessageRepositoryCustom {

	List<ChatMessage> findByChatRoomId(Long chatRoomId);

	int countUnreadMessages(Long chatRoomId, String senderId);

	void markMessagesAsRead(Long chatRoomId, String userId);

	Optional<ChatMessage> findTopByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);

	int countByChatRoomAndSenderIdNotAndStatus(ChatRoom chatRoom, String senderId, MessageStatus status);

	ChatMessage save(ChatMessage chatMessage);

	List<ChatMessage> saveAll(List<ChatMessage> chatMessages);

	Map<Long, ChatMessage> findLatestMessagesForChatRooms(Set<Long> chatRoomIds);
}
