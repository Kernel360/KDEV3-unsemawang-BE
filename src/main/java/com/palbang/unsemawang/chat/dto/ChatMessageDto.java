package com.palbang.unsemawang.chat.dto;

import com.palbang.unsemawang.chat.entity.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
	private Long chatRoomId;  // ✅ chatRoom 객체 대신 ID만 저장
	private String senderId;
	private String content;
	private MessageStatus status;
	private Long timestamp;
}
