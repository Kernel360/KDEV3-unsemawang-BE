package com.palbang.unsemawang.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChatMessageDto {
	private String message;

	public static NewChatMessageDto of(String message) {
		return new NewChatMessageDto(message);
	}
}
