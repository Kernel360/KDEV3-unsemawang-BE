package com.palbang.unsemawang.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.palbang.unsemawang.chat.constant.SenderType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatMessageResponse {
	private String content;

	private SenderType senderType;

	@JsonAlias("userId")
	private String partnerId;

	@JsonAlias("nickname")
	private String partnerNickname;

	@JsonAlias("profileImageUrl")
	private String partnerProfileImageUrl;

	private LocalDateTime timestamp;
}
