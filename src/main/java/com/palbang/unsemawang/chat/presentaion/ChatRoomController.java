package com.palbang.unsemawang.chat.presentaion;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.application.ChatFacade;
import com.palbang.unsemawang.chat.application.dto.ChatHistoryReadResponse;
import com.palbang.unsemawang.chat.application.dto.ChatRoomRequestDto;
import com.palbang.unsemawang.chat.application.dto.ChatRoomResponseDto;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatFacade chatFacade;

	/**
	 * 채팅방 생성 또는 기존 채팅방 반환
	 */
	@Operation(summary = "채팅방 생성", description = "사용자 간 1:1 채팅방을 생성하거나 기존 채팅방을 가져옵니다.")
	@PostMapping("/rooms")
	public ResponseEntity<ChatRoomResponseDto> createChatRoom(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestBody ChatRoomRequestDto requestDto) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (requestDto.getPartnerId() == null || requestDto.getPartnerId().isBlank()) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "partnerId가 누락되었습니다.");
		}

		ChatRoomResponseDto chatRoom = chatFacade.createChatRoom(auth.getId(), requestDto.getPartnerId());
		return ResponseEntity.ok(chatRoom);
	}

	/**
	 * 사용자의 채팅방 목록 조회
	 */
	@Operation(summary = "사용자의 채팅방 목록 조회", description = "사용자가 참여 중인 채팅방 목록을 조회합니다.")
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomResponseDto>> getUserChatRooms(
		@AuthenticationPrincipal CustomOAuth2User auth) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		List<ChatRoomResponseDto> chatRooms = chatFacade.getUserChatRooms(auth.getId());

		return ResponseEntity.ok(chatRooms);
	}

	/**
	 * 특정 채팅방 입장 (채팅 내역 불러오기)
	 */
	@Operation(summary = "채팅방 입장", description = "채팅방에 입장하며 기존 채팅 내역을 조회하고 안 읽은 메시지를 읽음 처리합니다.")
	@GetMapping("/{chatRoomId}/enter")
	public ResponseEntity<ChatHistoryReadResponse> enterChatRoom(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long chatRoomId) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		ChatHistoryReadResponse chatHistory = chatFacade.enterChatRoom(auth.getId(), chatRoomId);
		return ResponseEntity.ok(chatHistory);
	}

	/**
	 * 채팅방 나가기
	 */
	@DeleteMapping("/{chatRoomId}/leave")
	public ResponseEntity<Void> leaveChatRoom(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long chatRoomId) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}
		chatFacade.leaveChatRoom(auth.getId(), chatRoomId);
		return ResponseEntity.noContent().build();
	}
}
