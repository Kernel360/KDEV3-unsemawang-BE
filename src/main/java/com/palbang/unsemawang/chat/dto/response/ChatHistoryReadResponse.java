package com.palbang.unsemawang.chat.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

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
	@JsonAlias("partner")
	String partnerNickname;

	List<ChatMessageResponse> messages;
}
