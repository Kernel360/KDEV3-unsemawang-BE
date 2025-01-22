package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.service.CommentService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티 댓글")
@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@Operation(
		description = "커서 기반 페이징 처리된 댓글 조회 기능입니다. 처음 조회시에는 cursorKey를 null로 보내시면 됩니다",
		summary = "커서 기반 페이징 댓글 조회"
	)
	@GetMapping()
	public ResponseEntity<LongCursorResponse<CommentReadResponse>> getAllCommentsByPostId(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long postId,
		@RequestParam(required = false) Long cursorKey,
		@RequestParam(defaultValue = "10") Integer size) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}
		// cursorRequest 객체 생성
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorKey, size);

		LongCursorResponse<CommentReadResponse> response = commentService.getAllCommentsByPostId(postId, cursorRequest);

		return ResponseEntity.ok(response);
	}
}