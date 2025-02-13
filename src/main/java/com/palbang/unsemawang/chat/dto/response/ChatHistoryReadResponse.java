package com.palbang.unsemawang.chat.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;

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

	@Schema(required = false, description = "채팅 상대 닉네임")
	@JsonProperty("partner")
	String partnerNickname;

	@Schema(required = false, description = "채팅 상대 ID")
	String partnerId;

	@Schema(required = false, description = "채팅 메세지 목록")
	List<ChatMessageDto> messages;

	@Schema(required = false, description = "나가기 여부")
	Boolean isOut = false;
}
