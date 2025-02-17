package com.palbang.unsemawang.chat.infrastructure;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.interfaces.ChatMessageStore;
import com.palbang.unsemawang.chat.domain.repository.ChatMessageRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageStoreImpl implements ChatMessageStore {

	private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;

	@Transactional
	@Override
	public void markMessagesAsRead(Long chatRoomId, String userId) {
		chatMessageRepositoryCustom.markMessagesAsRead(chatRoomId, userId);
	}

	@Override
	public ChatMessage save(ChatMessage chatMessage) {
		chatMessageRepositoryCustom.save(chatMessage);
		return chatMessage;
	}

	@Override
	public List<ChatMessage> saveAll(List<ChatMessage> chatMessages) {
		return chatMessageRepositoryCustom.saveAll(chatMessages);
	}
}
