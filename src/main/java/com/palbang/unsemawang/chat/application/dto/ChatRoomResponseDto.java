package com.palbang.unsemawang.chat.application.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "채팅방 응답 DTO")
public class ChatRoomResponseDto {

	@Schema(required = true)
	private Long chatRoomId;

	@Schema(required = true)
	private String userId;

	@Schema(required = true)
	private String nickname;

	@Schema(required = true)
	private String profileImageUrl;

	@Schema(required = true)
	private String lastChat;

	@Schema(required = true)
	private LocalDateTime lastChatTime;

	@Schema(required = true)
	private char sex;

	@Schema(required = true)
	private String five;

	@Schema(required = true)
	private int unreadCount;
}
