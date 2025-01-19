package com.palbang.unsemawang.community.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.community.dto.response.CommentReadResponse;
import com.palbang.unsemawang.community.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티 댓글")
@RestController("/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@GetMapping()
	public ResponseEntity<Page<CommentReadResponse>> getComments(
		@RequestParam Long postId,
		@PageableDefault(size = 10) Pageable pageable) {
		Page<CommentReadResponse> comments = commentService.getCommentsByPostId(postId, pageable);
		return ResponseEntity.ok(comments);
	}
}
