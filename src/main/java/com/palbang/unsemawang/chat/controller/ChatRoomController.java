package com.palbang.unsemawang.chat.controller;

import java.util.List;
import java.util.Map;

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

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.ChatRoomDto;
import com.palbang.unsemawang.chat.service.ChatRoomService;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final RedisTemplate<String, String> redisTemplate;

	@PostMapping("/rooms")
	public ResponseEntity<ChatRoomDto> createOrGetChatRoom(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestBody Map<String, String> requestBody
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		String receiverId = requestBody.get("receiverId");
		ChatRoomDto chatRoom = chatRoomService.createOrGetChatRoom(auth.getId(), receiverId);
		return ResponseEntity.ok(chatRoom);
	}

	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@AuthenticationPrincipal CustomOAuth2User auth) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 사용자의 참여 채팅방을 조회하며, 마지막 메시지 및 오행 정보를 포함
		List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomsWithLastMessage(auth.getId());

		return ResponseEntity.ok(chatRooms);
	}

	/** ✅ 채팅방 입장: 기존 채팅 내역 조회 + 안 읽은 메시지 읽음 처리 */
	@GetMapping("/{chatRoomId}/enter")
	public ResponseEntity<List<ChatMessageDto>> enterChatRoom(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 1️⃣ 기존 채팅 내역 불러오기
		List<ChatMessageDto> chatHistory = chatRoomService.getChatHistory(chatRoomId, user.getId());

		// 2️⃣ 안 읽은 메시지를 모두 READ로 변경
		chatRoomService.markMessagesAsRead(chatRoomId, user.getId());

		return ResponseEntity.ok(chatHistory);
	}

	// 사용자가 채팅방 나가기
	@DeleteMapping("/{chatRoomId}/leave")
	public ResponseEntity<Void> leaveChatRoom(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		if (user == null || user.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}
		chatRoomService.leaveChatRoom(user.getId(), chatRoomId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user-status/{userId}")
	public ResponseEntity<Boolean> isUserOnline(@PathVariable String userId) {
		String isOnline = redisTemplate.opsForValue().get("online:" + userId);
		return ResponseEntity.ok(isOnline != null); // ✅ 키가 있으면 true, 없으면 false 반환
	}
}