package com.palbang.unsemawang.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	// 사용자 ID로 메시지 검색
	List<ChatMessage> findBySender(String sender);

	// 특정 메시지 유형으로 검색
	List<ChatMessage> findByType(String type);
}