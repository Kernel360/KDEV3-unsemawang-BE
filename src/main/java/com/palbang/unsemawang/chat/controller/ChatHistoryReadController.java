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

@RestController
@RequestMapping("/chemistry/chat-room")
public class ChatHistoryReadController {

	@GetMapping("/{chatRoomId}/history")
	public ResponseEntity<List<ChatHistoryReadResponse>> readChatHistory(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		List<ChatHistoryReadResponse> chatHistory = new LinkedList<>();

		return ResponseEntity.ok().body(chatHistory);
	}
}
