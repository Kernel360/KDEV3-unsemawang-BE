package com.palbang.unsemawang.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@EntityGraph(attributePaths = {"sender"})
	List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);

	// ✅ 채팅방의 마지막 메시지를 가져오는 메서드 추가 (sender 즉시 로딩)
	@EntityGraph(attributePaths = {"sender"})
	Optional<ChatMessage> findTopByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);

	// ✅ 특정 사용자가 안 읽은 메시지 개수 가져오기
	@Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatRoom = :chatRoom AND m.sender.id <> :userId AND m.status = :status")
	int countByChatRoomAndSenderIdNotAndStatus(
		@Param("chatRoom") ChatRoom chatRoom,
		@Param("userId") String userId,
		@Param("status") MessageStatus status
	);

	// ✅ 특정 사용자가 안 읽은 메시지 목록 가져오기
	@Query("SELECT m FROM ChatMessage m WHERE m.chatRoom = :chatRoom AND m.sender.id <> :userId AND m.status = :status")
	List<ChatMessage> findByChatRoomAndSenderIdNotAndStatus(
		@Param("chatRoom") ChatRoom chatRoom,
		@Param("userId") String userId,
		@Param("status") MessageStatus status
	);
}

