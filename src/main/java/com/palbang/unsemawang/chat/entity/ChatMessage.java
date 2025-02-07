package com.palbang.unsemawang.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;               // 메시지 ID (자동 생성)

	private String sender;         // 발신자
	private String content;        // 메시지 내용
	private String type;           // 메시지 유형 (CHAT, JOIN, LEAVE 등)

	private Long timestamp;        // 메시지 전송 시각 (epoch time)

	@Enumerated(EnumType.STRING)
	private MessageStatus status;  // 메시지 상태 (SENT, DELIVERED, READ)

	// 생성자 오버로드
	public ChatMessage(String sender, String content, String type) {
		this.sender = sender;
		this.content = content;
		this.type = type;
		this.timestamp = System.currentTimeMillis();
		this.status = MessageStatus.SENT; // 기본 상태: SENT
	}
}