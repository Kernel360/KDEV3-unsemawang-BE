package com.palbang.unsemawang.chat.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.palbang.unsemawang.chat.constant.MessageStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "채팅 메시지 응답 DTO")
public class ChatMessageResponseDto {

	@Schema(required = true, description = "채팅방 ID")
	private Long chatRoomId;

	@Schema(required = true, description = "보낸 사람 ID")
	private String senderId;

	@Schema(required = true, description = "메시지 내용")
	private String content;

	@Schema(required = true, description = "메시지 상태 (READ/UNREAD)")
	private MessageStatus status;

	@Schema(required = false, description = "메시지 타임스탬프 (밀리초)")
	private Long timestamp;

	@Schema(required = true, description = "보낸 사람 닉네임")
	private String nickname;

	@Schema(required = true, description = "보낸 사람 프로필 이미지 URL")
	private String profileImageUrl;
}
