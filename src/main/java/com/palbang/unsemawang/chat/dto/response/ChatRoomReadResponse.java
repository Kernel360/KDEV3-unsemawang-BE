package com.palbang.unsemawang.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(required = true, description = "채팅방 고유번호")
	private Long chatRoomId;

	@Schema(required = true, description = "상대방 ID")
	@JsonProperty("userId")
	private String partnerId;

	@Schema(required = true, description = "상대방 닉네임")
	@JsonProperty("nickname")
	private String partnerNickname;

	@Schema(required = true, description = "상대방 프로필 이미지 url")
	@JsonProperty("profileImageUrl")
	private String partnerProfileImageUrl;

	@Schema(required = true, description = "상대방 성별")
	@JsonProperty("sex")
	private String partnerSex;

	@Schema(required = true, description = "상대방 오행(일주) 정보")
	@JsonProperty("five")
	private String partnerFiveElementCn;

	@Schema(required = false, description = "마지막으로 보낸 채팅 메세지")
	@JsonProperty("lastChat")
	private String lastChatMessage;

	@Schema(required = false, description = "마지막으로 보낸 채팅 일시")
	private LocalDateTime lastChatTime;
}
