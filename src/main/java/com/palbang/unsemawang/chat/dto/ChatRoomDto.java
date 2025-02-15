package com.palbang.unsemawang.chat.dto;

import java.time.LocalDateTime;

import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "채팅방 DTO")
public class ChatRoomDto {

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

	public static ChatRoomDto fromEntity(ChatRoom chatRoom, ChatMessage lastMessage, Member targetUser,
		String fiveElement, char sex, int unreadCount, String profileImageUrl) {

		// targetUser의 정보가 없는 경우 기본값 설정
		String userId = (targetUser != null) ? targetUser.getId() : "unknown";
		String nickname = (targetUser != null) ? targetUser.getNickname() : "알 수 없음";

		LocalDateTime lastMessageTime = lastMessage != null ? lastMessage.getTimestamp() : chatRoom.getCreatedAt();
		boolean isReadOnly = (chatRoom.getUser1() == null || chatRoom.getUser2() == null);

		return ChatRoomDto.builder()
			.chatRoomId(chatRoom.getId())
			.userId(userId)
			.nickname(nickname)
			.profileImageUrl(profileImageUrl) // ChatRoomService에서 조회한 프로필 이미지 사용
			.lastChat(lastMessage != null ? lastMessage.getContent() : "")
			.lastChatTime(lastMessageTime)
			.sex(sex)
			.five(fiveElement)
			.unreadCount(unreadCount)
			.build();
	}

}
