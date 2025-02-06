package com.palbang.unsemawang.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
	private String sender;    // 발신자 ID
	private String content;   // 메시지 내용
	private String type;      // 메시지 타입 (CHAT, JOIN, LEAVE 등)
}