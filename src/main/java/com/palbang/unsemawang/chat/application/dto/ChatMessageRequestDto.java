package com.palbang.unsemawang.chat.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

	@Schema(required = true, description = "메시지 내용")
	private String message;
}
