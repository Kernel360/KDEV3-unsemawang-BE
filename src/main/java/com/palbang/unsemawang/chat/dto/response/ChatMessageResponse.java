package com.palbang.unsemawang.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palbang.unsemawang.chat.constant.SenderType;

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
public class ChatMessageResponse {
	@Schema(required = true, description = "채팅 메세지 내용")
	private String content;

	@Schema(required = true, description = "보낸 사람 타입")
	private SenderType senderType;

	@Schema(required = false, description = "보낸 사람 ID")
	@JsonAlias("userId")
	private String senderId;

	@Schema(required = false, description = "보낸 사람 닉네임")
	@JsonProperty("nickname")
	private String senderNickname;

	@Schema(required = false, description = "보낸 사람 프로필 사진")
	@JsonProperty("profileImageUrl")
	private String senderProfileImageUrl;

	@Schema(required = true, description = "보낸 일시")
	private LocalDateTime timestamp;
}
