package com.palbang.unsemawang.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@EntityGraph(attributePaths = {"sender"})
	List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);

	// 채팅방의 마지막 메시지를 가져오는 메서드 추가 ✅ sender를 즉시 로딩
	@EntityGraph(attributePaths = {"sender"})
	Optional<ChatMessage> findTopByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);
}
