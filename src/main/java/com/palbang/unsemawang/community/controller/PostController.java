package com.palbang.unsemawang.community.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "커뮤니티")
public interface PostController {
	/* 게시글 등록 */
	@Operation(
		description = "커뮤니티 게시글 등록 API 입니다. 인증 토큰이 담긴 쿠키를 직접 보내셔야 테스트가 가능합니다!",
		summary = "커뮤니티 게시글 등록"
	)
	@ApiResponse(responseCode = "201", description = "이미지 업로드 및 게시글 등록 성공")
	@ApiResponse(responseCode = "413", description = "이미지 용량 초과로 등록 실패")
	@ApiResponse(responseCode = "400", description = "권한이 없는 회원이거나 유효하지 않는 데이터로 등록 실패")
	ResponseEntity<PostRegisterResponse> writeCommunityPost(
		CustomOAuth2User auth,
		List<MultipartFile> fileList,
		PostRegisterRequest postRegisterRequest
	);

	/* 게시글 수정 */
	@Operation(
		description = "커뮤니티 게시글 수정 API 입니다. 인증 토큰이 담긴 쿠키를 보내셔야 테스트가 가능합니다!",
		summary = "커뮤니티 게시글 수정"
	)
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity modifyCommunityPost(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId,
		@Valid @RequestBody PostUpdateRequest postUpdateRequest
	);

	/* 게시글 삭제 */
	@Operation(
		description = "커뮤니티 게시글 삭제 API 입니다",
		summary = "커뮤니티 게시글 삭제"
	)
	@ApiResponse(responseCode = "204", description = "게시글 삭제 완료")
	@ApiResponse(responseCode = "400", description = "권한이 없는 회원이거나 유효하지 않는 게시글로 삭제 실패")
	@DeleteMapping(path = "/{id}")
	ResponseEntity deleteCommunityPost(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@PathVariable("id") Long postId
	);
}
