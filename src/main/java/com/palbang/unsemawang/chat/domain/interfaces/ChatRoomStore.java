package com.palbang.unsemawang.chat.domain.interfaces;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;

public interface ChatRoomStore {
	ChatRoom save(ChatRoom chatRoom);
}
