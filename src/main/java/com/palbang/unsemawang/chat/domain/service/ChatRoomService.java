package com.palbang.unsemawang.chat.domain.service;

import java.util.List;

import com.palbang.unsemawang.chat.domain.info.ChatHistoryReadInfo;
import com.palbang.unsemawang.chat.domain.info.ChatRoomInfo;

public interface ChatRoomService {

	ChatRoomInfo createChatRoom(String userId, String partnerId);

	List<ChatRoomInfo> getUserChatRooms(String userId);

	ChatRoomInfo getChatRoomInfo(Long chatRoomId);

	ChatHistoryReadInfo enterChatRoom(String userId, Long chatRoomId);

	void leaveChatRoom(String userId, Long chatRoomId);
}
