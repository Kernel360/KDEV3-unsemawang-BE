package com.palbang.unsemawang.community.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "커뮤니티 게시글")
public interface PostController {
	/* 게시글 등록 */
	@Operation(
		description = "커뮤니티 게시글 등록 API 입니다. 인증 토큰이 담긴 쿠키를 직접 보내셔야 테스트가 가능합니다!",
		summary = "커뮤니티 게시글 등록"
	)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostRegisterResponse> write(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody PostRegisterRequest postRegisterRequest
	);

	/* 게시글 수정 */
	@Operation(
		description = "커뮤니티 게시글 수정 API 입니다. 인증 토큰이 담긴 쿠키를 보내셔야 테스트가 가능합니다!",
		summary = "커뮤니티 게시글 수정"
	)
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity modify(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId,
		@Valid @RequestBody PostUpdateRequest postUpdateRequest
	);

	/* 게시글 삭제 */
	@Operation(
		description = "커뮤니티 게시글 삭제 API 입니다",
		summary = "커뮤니티 게시글 삭제"
	)
	@DeleteMapping(path = "/{id}")
	ResponseEntity delete(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId
	);
}
