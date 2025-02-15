package com.palbang.unsemawang.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChatMessageDto {
	private String message;
	private LocalDateTime lastChatTime;

	public static NewChatMessageDto of(String message, LocalDateTime lastChatTime) {
		return new NewChatMessageDto(message, lastChatTime);
	}
}
