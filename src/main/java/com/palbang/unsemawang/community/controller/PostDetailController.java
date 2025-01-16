package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

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
	public ResponseEntity<PostDetailResponse> getPostDetail(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable Long id) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		PostDetailResponse response = postDetailService.getPostDetail(auth.getId(), id);

		return ResponseEntity.ok(response);
	}
}
