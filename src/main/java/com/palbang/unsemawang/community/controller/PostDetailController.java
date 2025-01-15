package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostDetailController {

	private final PostDetailService postDetailService;

	@GetMapping(path = "/{id}")
	@Operation(summary = "게시글 상세 조회")
	public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long id) {
		PostDetailResponse response = postDetailService.getPostDetail(id);
		return ResponseEntity.ok(response);
	}
}
