package com.palbang.unsemawang.chat.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.dto.response.ChatHistoryReadResponse;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "궁합 채팅")
@RestController
@RequestMapping("/chemistry/chat-room")
public class ChatHistoryReadController {

	@Operation(
		description = "특정 채팅방의 지난 채팅 내역을 조회합니다",
		summary = "지난 채팅 내역 조회"
	)
	@GetMapping("/{chatRoomId}/history")
	public ResponseEntity<ChatHistoryReadResponse> readChatHistory(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		ChatHistoryReadResponse chatHistory = ChatHistoryReadResponse.builder().build();

		return ResponseEntity.ok().body(chatHistory);
	}
}
