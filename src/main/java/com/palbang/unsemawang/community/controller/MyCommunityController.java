package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.response.MyCommentsReadResponse;
import com.palbang.unsemawang.community.service.MyCommunityService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyCommunityController {

	private final MyCommunityService myService;

	@Operation(
		description = "나의 커뮤니티 댓글 조회 API 입니다.",
		summary = "커뮤니티 내 댓글 목록 조회"
	)
	@GetMapping("/comments")
	public ResponseEntity<LongCursorResponse<MyCommentsReadResponse>> readMyCommentsList(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestParam(required = false) Long cursorKey,
		@RequestParam(defaultValue = "10") Integer size
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// cursorRequest 객체 생성
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorKey, size);

		LongCursorResponse<MyCommentsReadResponse> response = myService.commentListRead(cursorRequest, auth.getId());

		return ResponseEntity.ok(response);
	}
}
