package com.palbang.unsemawang.chat.domain.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.palbang.unsemawang.chat.domain.entity.ChatMessage;

public interface ChatMessageReader {
	List<ChatMessage> findByChatRoomId(Long chatRoomId);

	int countUnreadMessages(Long chatRoomId, String senderId);

	Map<Long, ChatMessage> findLatestMessagesForChatRooms(Set<Long> chatRoomIds);
}
