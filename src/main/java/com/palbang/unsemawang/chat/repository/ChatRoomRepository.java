package com.palbang.unsemawang.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	// 두 사용자의 ID를 정렬한 후 조회
	@Query("SELECT c FROM ChatRoom c WHERE " +
		"(c.user1.id = :user1Id AND c.user2.id = :user2Id) " +
		"OR (c.user1.id = :user2Id AND c.user2.id = :user1Id)")
	Optional<ChatRoom> findByUsers(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);

	@Query("SELECT c FROM ChatRoom c WHERE c.user1.id = :userId OR c.user2.id = :userId")
	List<ChatRoom> findByUserId(@Param("userId") String userId);

	// ChatRoomRepository
	@Query("SELECT CASE " +
		"WHEN c.user1.id = :userId THEN c.user2.id " +
		"WHEN c.user2.id = :userId THEN c.user1.id " +
		"ELSE NULL END " +
		"FROM ChatRoom c WHERE c.id = :chatRoomId")
	Optional<String> findOtherMemberIdInChatRoom(@Param("chatRoomId") Long chatRoomId, @Param("userId") String userId);
}
