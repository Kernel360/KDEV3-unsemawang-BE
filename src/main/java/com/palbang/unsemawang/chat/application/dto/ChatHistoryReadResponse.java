package com.palbang.unsemawang.chat.application.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatHistoryReadResponse {

	@Schema(required = true, description = "채팅 상대 닉네임")
	@JsonProperty("partner")
	String partnerNickname;

	@Schema(required = true, description = "채팅 상대 ID")
	String partnerId;

	@Schema(required = true, description = "채팅 메세지 목록")
	List<ChatMessageResponseDto> messages;
}
