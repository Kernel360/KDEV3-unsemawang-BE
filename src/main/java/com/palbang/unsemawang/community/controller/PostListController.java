package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
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

	@Operation(
			description = """
			sort(정렬 분류) : latest || mostViewed <br>
			카테고리 미지정 시 인기 게시글 조회 <br>
			인기 게시글 조회 시 sort = null
			""",
			summary = "게시글 목록 조회")
	@GetMapping("/posts")
	public ResponseEntity<LongCursorResponse<PostListResponse>> getPostList(
		@RequestParam(required = false) CommunityCategory category,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false, defaultValue = "10") Integer size,
		@RequestParam(required = false, defaultValue = "LATEST") Sortingtype sort
	) {
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorId, size);
		LongCursorResponse<PostListResponse> response;

		// 인기 게시판 요청이 따로 들어오면 처리
		if (category == null) { // 또는 별도 필드에 따라 판단해도 됨
			response = postListService.getPopularPosts(cursorRequest);
		} else {
			// 카테고리 기반의 기본 게시판 처리
			response = postListService.getPostList(category, sort, cursorRequest);
		}

		return ResponseEntity.ok(response);
	}

	@Operation(
			description = """
			검색 및 페이지네이션 기반 게시글 목록 조회<br>
			searchType: all, title, content, writer (기본값은 all)
			""",
			summary = "게시글 검색")
	@GetMapping("/posts/search")
	public ResponseEntity<LongCursorResponse<PostListResponse>> searchPosts(
			@RequestParam String keyword,
			@RequestParam(required = false, defaultValue = "all") String searchType,
			@RequestParam(required = false) Long cursorId,
			@RequestParam(required = false, defaultValue = "10") Integer size
	) {
		// CursorRequest 생성
		CursorRequest<Long> cursorRequest = new CursorRequest<>(cursorId, size);

		// 서비스 호출
		LongCursorResponse<PostListResponse> response = postListService.searchPosts(
				keyword,
				searchType,
				cursorRequest
		);

		return ResponseEntity.ok(response);
	}

}
