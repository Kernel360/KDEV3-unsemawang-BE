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
	private Long chatRoomId;
	private String userId;
	private String nickname;
	private String profileImageUrl;
	private String lastChat;
	private LocalDateTime lastChatTime;
	private String sex;
	private String five;
	private int unreadCount;
	private boolean isReadOnly;  // 읽기 전용 여부 추가

	public static ChatRoomDto fromEntity(ChatRoom chatRoom, ChatMessage lastMessage, Member targetUser,
		String fiveElement, int unreadCount) {

		// 현재 채팅방에 남아 있는 사용자가 혼자이면 isReadOnly = true
		boolean isReadOnly = (chatRoom.getUser1() == null || chatRoom.getUser2() == null);

		String userId = (targetUser != null) ? targetUser.getId() : "unknown";
		String nickname = (targetUser != null) ? targetUser.getNickname() : "알 수 없음";
		String profileUrl =
			(targetUser != null) ? targetUser.getProfileUrl() : "https://cdn.example.com/default-profile.png";
		char gender = (targetUser != null) ? targetUser.getGender() : 'N';

		LocalDateTime lastMessageTime = lastMessage != null ? lastMessage.getTimestamp() : chatRoom.getCreatedAt();

		return ChatRoomDto.builder()
			.chatRoomId(chatRoom.getId())
			.userId(userId)
			.nickname(nickname)
			.profileImageUrl(profileUrl)
			.lastChat(lastMessage != null ? lastMessage.getContent() : "")
			.lastChatTime(lastMessageTime)
			.sex(gender == 'M' ? "남" : "여")
			.five(fiveElement)
			.unreadCount(unreadCount)
			.isReadOnly(isReadOnly)  // 읽기 전용 여부 설정
			.build();
	}
}
