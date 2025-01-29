package com.palbang.unsemawang.community.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.request.PostDeleteRequest;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.dto.response.PostUpdateResponse;
import com.palbang.unsemawang.community.entity.Post;
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
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Override
	public ResponseEntity<PostRegisterResponse> writeCommunityPost(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFileList,
		@Valid @RequestPart(value = "postDetail") PostRegisterRequest postRegisterRequest
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		postRegisterRequest.updateMemberId(auth.getId());

		Post savedPost = postService.register(postRegisterRequest, imageFileList);

		return ResponseEntity
			.created(URI.create("/posts/" + savedPost.getId()))
			.body(PostRegisterResponse.of(savedPost.getId()));
	}

	/* 게시글 수정 */
	@PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Override
	public ResponseEntity<PostUpdateResponse> modifyCommunityPost(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId,
		@RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFileList,
		@Valid @RequestPart(value = "postDetail") PostUpdateRequest postUpdateRequest
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 게시글 업데이트
		Post updatedPost = postService.updatePostAndImgFiles(auth.getId(), postId, postUpdateRequest, imageFileList);

		return ResponseEntity
			.ok()
			.body(PostUpdateResponse.of(updatedPost.getId()));
	}

	/* 게시글 삭제 */
	@DeleteMapping(path = "/{id}")
	@Override
	public ResponseEntity deleteCommunityPost(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		postService.delete(PostDeleteRequest.of(postId, auth.getId()));

		return ResponseEntity.noContent().build();
	}

}
