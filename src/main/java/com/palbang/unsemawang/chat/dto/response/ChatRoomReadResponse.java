package com.palbang.unsemawang.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoomReadResponse {

	private Long chatRoomId;

	@JsonAlias("userId")
	private String partnerId;

	@JsonAlias("nickname")
	private String partnerNickname;

	@JsonAlias("profileImageUrl")
	private String partnerProfileImageUrl;

	@JsonAlias("sex")
	private String partnerSex;

	@JsonAlias("five")
	private String partnerFiveElementCn;

	@JsonAlias("lastChat")
	private String lastChatMessage;
	private LocalDateTime lastChatTime;
}
