package com.palbang.unsemawang.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.entity.MessageStatus;

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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "채팅 메시지 DTO")
public class ChatMessageDto {

	@Schema(required = true)
	private Long chatRoomId;
	@Schema(required = true)
	private String senderId;
	@Schema(required = true)
	private String content;
	@Schema(required = true)
	private MessageStatus status;
	@Schema(required = false)
	private Long timestamp;
	@Schema(required = true)
	private String nickname;
	@Schema(required = true)
	private String profileImageUrl;

	/** 보낸 사람 타입 (SELF: 본인, OTHER: 상대방) */
	@JsonProperty("senderType")
	@Schema(required = true)
	private SenderType senderType;
}
