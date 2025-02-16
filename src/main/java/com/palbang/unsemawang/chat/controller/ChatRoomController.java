package com.palbang.unsemawang.chat.controller;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.dto.ChatRoomDto;
import com.palbang.unsemawang.chat.dto.request.ChatRoomCreateRequest;
import com.palbang.unsemawang.chat.dto.response.ChatHistoryReadResponse;
import com.palbang.unsemawang.chat.service.ChatRoomService;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final RedisTemplate<String, String> redisTemplate;

	@Operation(summary = "채팅방 생성 또는 가져오기", description = "사용자 간 1:1 채팅방을 생성하거나 기존 채팅방을 가져옵니다.")
	@PostMapping("/rooms")
	public ResponseEntity<ChatRoomDto> createOrGetChatRoom(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestBody ChatRoomCreateRequest request

	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (request.getPartnerId() == null || request.getPartnerId().isBlank()) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "partnerId가 누락되었습니다.");
		}

		ChatRoomDto chatRoom = chatRoomService.createOrGetChatRoom(auth.getId(), request.getPartnerId());
		return ResponseEntity.ok(chatRoom);
	}

	@Operation(summary = "사용자의 채팅방 목록 조회", description = "사용자가 참여 중인 채팅방 목록을 조회합니다.")
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@AuthenticationPrincipal CustomOAuth2User auth) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomsWithLastMessage(auth.getId());

		return ResponseEntity.ok(chatRooms);
	}

	@Operation(summary = "채팅방 입장", description = "채팅방에 입장하며 기존 채팅 내역을 조회하고 안 읽은 메시지를 읽음 처리합니다.")
	@GetMapping("/{chatRoomId}/enter")
	public ResponseEntity<ChatHistoryReadResponse> enterChatRoom(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		ChatHistoryReadResponse chatHistory = chatRoomService.getChatHistory(chatRoomId, user.getId());
		chatRoomService.markMessagesAsRead(chatRoomId, user.getId());

		return ResponseEntity.ok(chatHistory);
	}

	@Operation(summary = "채팅방 나가기", description = "사용자가 채팅방을 나가고 해당 방에서 더 이상 채팅을 할 수 없도록 처리합니다.")
	@DeleteMapping("/{chatRoomId}/leave")
	public ResponseEntity<Void> leaveChatRoom(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		chatRoomService.leaveChatRoom(user.getId(), chatRoomId);
		return ResponseEntity.noContent().build();
	}
}