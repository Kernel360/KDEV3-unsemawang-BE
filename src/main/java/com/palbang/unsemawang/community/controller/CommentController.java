package com.palbang.unsemawang.community.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.request.CommentRegisterRequest;
import com.palbang.unsemawang.community.dto.request.CommentUpdateRequest;
import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.service.CommentService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@Operation(description = "커서 기반 페이징 처리된 댓글 조회 기능입니다. 처음 조회시에는 cursorKey를 null로 보내시면 됩니다", summary = "댓글 조회 (커서 기반 페이징)")
	@GetMapping
	public ResponseEntity<LongCursorResponse<CommentReadResponse>> getAllCommentsByPostId(
		@AuthenticationPrincipal CustomOAuth2User auth, @RequestParam Long postId,
		@RequestParam(required = false) Long cursorKey, @RequestParam(defaultValue = "10") Integer size) {

		String memberId = null; // 비회원일 경우 null

		// 로그인한 사용자일 경우
		if (auth != null && auth.getId() != null) {
			memberId = auth.getId();
		}

		// cursorRequest 객체 생성
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorKey, size);

		LongCursorResponse<CommentReadResponse> response = commentService.getAllCommentsByPostId(postId, cursorRequest,
			memberId);

		return ResponseEntity.ok(response);
	}

	@Operation(description = "해당 게시글에 댓글을 등록할 수 있습니다. 댓글과 대댓글까지 작성할 수 있습니다.", summary = "댓글/대댓글 등록")
	@PostMapping
	public ResponseEntity<Void> registerCommentByPostId(@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestParam Long postId, @RequestBody @Valid CommentRegisterRequest request) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		commentService.registerComment(postId, request, auth.getId());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(description = "해당 게시글에 본인이 작성한 댓글/대댓글을 수정할 수 있습니다.", summary = "댓글/대댓글 수정")
	@PutMapping("/{commentId}")
	public ResponseEntity<Void> updateCommentByPostId(@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest request) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		commentService.updateComment(commentId, request, auth.getId());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(description = "내가 작성한 댓글을 삭제하는 기능입니다", summary = "댓글 삭제")
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteCommentByPostId(@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long commentId) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		commentService.deleteComment(commentId, auth.getId());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}