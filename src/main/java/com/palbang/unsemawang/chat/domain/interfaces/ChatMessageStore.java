package com.palbang.unsemawang.chat.domain.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chat.domain.entity.ChatMessage;

public interface ChatMessageStore {
	@Transactional
	void markMessagesAsRead(Long chatRoomId, String userId);

	ChatMessage save(ChatMessage chatMessage);

	List<ChatMessage> saveAll(List<ChatMessage> chatMessages);
}
