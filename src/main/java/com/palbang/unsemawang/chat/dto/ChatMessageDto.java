package com.palbang.unsemawang.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palbang.unsemawang.chat.constant.SenderType;
import com.palbang.unsemawang.chat.entity.MessageStatus;

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
public class ChatMessageDto {

	private Long chatRoomId;
	private String senderId;
	private String content;
	private MessageStatus status;
	private Long timestamp;
	private String nickname;
	private String profileImageUrl;

	/** 보낸 사람 타입 (SELF: 본인, OTHER: 상대방) */
	@JsonProperty("senderType")
	private SenderType senderType;
}
