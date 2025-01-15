package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostDetailController {

	private final PostDetailService postDetailService;

	@GetMapping(path = "/{id}")
	public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long id) {
		PostDetailResponse response = postDetailService.getPostDetail(id);
		return ResponseEntity.ok(response);
	}
}
