package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostRegisterResponse> write(@RequestBody PostRegisterRequest postRegisterRequest) {

		PostRegisterResponse postRegisterResponse = postService.register(postRegisterRequest);

		return ResponseEntity.ok(postRegisterResponse);
	}
}
