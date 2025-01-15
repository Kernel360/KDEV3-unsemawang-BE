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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Tag(name = "커뮤니티 게시글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@Operation(
		description = "커뮤니티 게시글 등록 API 입니다. 인증 토큰이 담긴 쿠키를 직접 보내셔야 테스트가 가능합니다!",
		summary = "커뮤니티 게시글 등록"
	)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PostRegisterResponse> write(
		@NotNull(message = "인증 객체가 없습니다") @AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody PostRegisterRequest postRegisterRequest
	) {
		postRegisterRequest.updateMemberId(auth.getId());

		PostRegisterResponse postRegisterResponse = postService.register(postRegisterRequest);

		return ResponseEntity.ok(postRegisterResponse);
	}
}
