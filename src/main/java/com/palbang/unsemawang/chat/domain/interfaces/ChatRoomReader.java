package com.palbang.unsemawang.chat.domain.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;

public interface ChatRoomReader {
	Optional<ChatRoom> findById(Long chatRoomId);

	Optional<ChatRoom> findByUser1AndUser2(String userId, String partnerId);

	String findOtherMemberIdInChatRoom(Long chatRoomId, String userId);

	List<ChatRoom> findByUserId(String userId);

	Map<Long, String> findOtherMemberIdsInChatRooms(Set<Long> chatRoomIds, String userId);
}
