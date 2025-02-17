package com.palbang.unsemawang.chat.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;

public interface ChatRoomRepositoryCustom {

	Optional<ChatRoom> findById(Long chatRoomId);

	Optional<ChatRoom> findByUser1AndUser2(String user1Id, String user2Id);

	List<ChatRoom> findByUserId(String userId);

	Optional<String> findOtherMemberIdInChatRoom(Long chatRoomId, String userId);

	ChatRoom save(ChatRoom chatRoom);

	Map<Long, String> findOtherMemberIdsInChatRooms(Set<Long> chatRoomIds, String userId);
}
