package com.palbang.unsemawang.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChatMessageCountDto {
	private int count;

	public static NewChatMessageCountDto of(int count) {
		return new NewChatMessageCountDto(count);
	}
}
