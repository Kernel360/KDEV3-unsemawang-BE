package com.palbang.unsemawang.chat.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.dto.response.ChatRoomReadResponse;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "궁합 채팅")
@RestController
@RequestMapping("/chemistry/chat-room")
public class ChatRoomReadController {

	@Operation(
		description = "나의 채팅방 목록을 조회합니다",
		summary = "나의 채팅방 목록 조회"
	)
	@GetMapping
	public ResponseEntity<List<ChatRoomReadResponse>> readMyChatRoomList(
		@AuthenticationPrincipal CustomOAuth2User user
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		List<ChatRoomReadResponse> myChatRoomList = new LinkedList<>();

		return ResponseEntity.ok().body(myChatRoomList);
	}
}
