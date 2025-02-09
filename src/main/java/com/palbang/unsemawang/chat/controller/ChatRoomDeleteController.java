package com.palbang.unsemawang.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "궁합 채팅")
@RestController
@RequestMapping("/chemistry/chat-room")
public class ChatRoomDeleteController {

	@Operation(
		description = "나의 채팅방 목록을 조회합니다",
		summary = "나의 채팅방 목록 조회"
	)
	@DeleteMapping("/{roomId}")
	public ResponseEntity<Void> readMyChatRoomList(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("roomId") Long roomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		return ResponseEntity.noContent().build();
	}
}
