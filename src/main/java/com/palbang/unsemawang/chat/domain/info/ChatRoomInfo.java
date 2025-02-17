package com.palbang.unsemawang.chat.domain.info;

import java.time.LocalDateTime;

import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomInfo {
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

	public static ChatRoomInfo fromEntity(ChatRoom chatRoom, ChatMessage lastMessage, Member targetUser,
		String fiveElement, char sex, int unreadCount, String profileImageUrl) {
		return ChatRoomInfo.builder()
			.chatRoomId(chatRoom.getId())
			.userId(targetUser != null ? targetUser.getId() : "unknown")
			.nickname(targetUser != null ? targetUser.getNickname() : "알 수 없음")
			.profileImageUrl(profileImageUrl)
			.lastChat(lastMessage != null ? lastMessage.getContent() : "")
			.lastChatTime(lastMessage != null ? lastMessage.getTimestamp() : chatRoom.getCreatedAt())
			.sex(sex)
			.five(fiveElement)
			.unreadCount(unreadCount)
			.build();
	}
}
