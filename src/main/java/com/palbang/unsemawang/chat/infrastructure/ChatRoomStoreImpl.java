package com.palbang.unsemawang.chat.infrastructure;

import org.springframework.stereotype.Component;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.interfaces.ChatRoomStore;
import com.palbang.unsemawang.chat.domain.repository.ChatRoomRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomStoreImpl implements ChatRoomStore {

	private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;

	@Override
	public ChatRoom save(ChatRoom chatRoom) {
		return chatRoomRepositoryCustom.save(chatRoom);
	}
}
