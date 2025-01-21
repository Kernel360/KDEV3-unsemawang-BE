package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.service.PostListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티")
@RestController
@RequiredArgsConstructor
public class PostListController {

	private final PostListService postListService;

	@Operation(summary = "게시글 목록 조회")
	@GetMapping("/posts")
	public ResponseEntity<LongCursorResponse<PostListResponse>> getPostList(
		@RequestParam CommunityCategory category,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false, defaultValue = "10") Integer size
	) {
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorId, size);
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, cursorRequest);
		return ResponseEntity.ok(response);
	}
}