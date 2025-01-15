package com.palbang.unsemawang.community.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.service.PostService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PostRegisterResponse> write(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody PostRegisterRequest postRegisterRequest
	) {
		postRegisterRequest.updateMemberId(auth.getId());

		PostRegisterResponse postRegisterResponse = postService.register(postRegisterRequest);

		return ResponseEntity.ok(postRegisterResponse);
	}
}
