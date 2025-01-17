package com.palbang.unsemawang.community.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.request.PostDeleteRequest;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.service.PostService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostControllerImpl implements PostController {

	private final PostService postService;

	/* 게시글 등록 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<PostRegisterResponse> write(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody PostRegisterRequest postRegisterRequest
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		postRegisterRequest.updateMemberId(auth.getId());

		PostRegisterResponse postRegisterResponse = postService.register(postRegisterRequest);

		return ResponseEntity.ok(postRegisterResponse);
	}

	/* 게시글 수정 */
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity modify(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId,
		@Valid @RequestBody PostUpdateRequest postUpdateRequest
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 게시글 업데이트
		postService.update(auth.getId(), postId, postUpdateRequest);

		return ResponseEntity.ok().build();
	}

	/* 게시글 삭제 */
	@DeleteMapping(path = "/{id}")
	@Override
	public ResponseEntity delete(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		postService.delete(PostDeleteRequest.of(postId, auth.getId()));

		return ResponseEntity.ok().build();
	}

}
