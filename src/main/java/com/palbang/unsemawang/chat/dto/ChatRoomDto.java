package com.palbang.unsemawang.chat.dto;

import java.time.LocalDateTime;

import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDto {

	private Long chatRoomId;
	private String userId;
	private String nickname;
	private String profileImageUrl;
	private String lastChat;
	private LocalDateTime lastChatTime;
	private String sex;
	private String five;
	private int unreadCount; // ✅ 필드 추가

	public static ChatRoomDto fromEntity(ChatRoom chatRoom, ChatMessage lastMessage, Member targetUser,
		String fiveElement) {
		if (targetUser == null) {
			throw new IllegalStateException("❌ ChatRoomDto 변환 중 대상 사용자가 null입니다. " +
				"chatRoomId=" + chatRoom.getId() + ", receiverId=null (확인 필요)");
		}

		// ✅ timestamp(Long)을 LocalDateTime으로 변환
		LocalDateTime lastMessageTime = lastMessage != null
			? lastMessage.getTimestamp()  // ✅ LocalDateTime을 직접 사용
			: chatRoom.getCreatedAt();

		return ChatRoomDto.builder()
			.chatRoomId(chatRoom.getId())
			.userId(targetUser.getId())
			.nickname(targetUser.getNickname())
			.profileImageUrl(targetUser.getProfileUrl())
			.lastChat(lastMessage != null ? lastMessage.getContent() : "")
			.lastChatTime(lastMessageTime) // ✅ LocalDateTime 사용
			.sex(targetUser.getGender() == 'M' ? "남" : "여")
			.five(fiveElement)
			.unreadCount(0) // 기본 값 설정 (ChatRoom에 unreadCount 필드 추가 필요)
			.build();
	}

}
