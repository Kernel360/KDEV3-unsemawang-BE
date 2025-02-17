package com.palbang.unsemawang.chat.domain.info;

import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.palbang.unsemawang.chat.constant.MessageStatus;
import com.palbang.unsemawang.chat.domain.entity.ChatMessage;

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
@Schema(description = "채팅 메시지 응답 DTO")
public class ChatMessageInfo {

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

	// 단일 엔티티 변환
	public static ChatMessageInfo fromEntity(ChatMessage chatMessage, String profileImageUrl) {

		return ChatMessageInfo.builder()
			.chatRoomId(chatMessage.getChatRoom().getId())
			.senderId(chatMessage.getSender().getId())
			.content(chatMessage.getContent())
			.status(MessageStatus.RECEIVED)
			.timestamp(chatMessage.getTimestamp()
				.atZone(ZoneId.systemDefault())
				.toInstant()
				.toEpochMilli())
			.nickname(chatMessage.getSender().getNickname())
			.profileImageUrl(profileImageUrl)
			.build();
	}

	// 리스트 변환
	public static List<ChatMessageInfo> fromEntityList(List<ChatMessage> chatMessages, String profileImageUrl) {
		return chatMessages.stream()
			.map(chatMessage -> fromEntity(chatMessage, profileImageUrl)) // ✅ profileImageUrl 전달
			.toList();
	}
}