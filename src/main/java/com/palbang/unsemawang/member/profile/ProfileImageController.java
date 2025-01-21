package com.palbang.unsemawang.member.profile;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.response.ErrorResponse;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로필 이미지 API", description = "회원의 프로필 이미지를 관리하는 API입니다.")
public interface ProfileImageController {

	@Operation(
		summary = "프로필 이미지 업로드",
		description = "회원의 프로필 이미지를 업로드합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "프로필 이미지 업로드 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 형식 오류)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "413", description = "파일 크기 초과 (File Too Large)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 오류 (파일 업로드 실패)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<Void> uploadProfileImage(@AuthenticationPrincipal CustomOAuth2User user,
		@Parameter(description = "업로드할 파일", required = true)
		@RequestParam("file") MultipartFile file);

	@Operation(
		summary = "프로필 이미지 수정",
		description = "회원의 프로필 이미지를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 형식 오류)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "413", description = "파일 크기 초과 (File Too Large)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 오류 (파일 수정 실패)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<Void> updateProfileImage(@AuthenticationPrincipal CustomOAuth2User user,
		@Parameter(description = "업로드할 파일", required = true)
		@RequestParam("file") MultipartFile file);

	@Operation(
		summary = "프로필 이미지 삭제",
		description = "회원의 프로필 이미지를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "프로필 이미지 삭제 성공"),
			@ApiResponse(responseCode = "404", description = "프로필 이미지 없음 (File Not Found)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 오류 (파일 삭제 실패)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@DeleteMapping("/profile-image")
	ResponseEntity<Void> deleteProfileImage(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(
		summary = "프로필 이미지 가져오기",
		description = "회원의 프로필 이미지를 가져옵니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "프로필 이미지 가져오기 성공"),
			@ApiResponse(responseCode = "404", description = "프로필 이미지 없음 (File Not Found)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 오류 (파일 조회 실패)",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)))
		}
	)
	@GetMapping("/profile-image")
	ResponseEntity<String> getProfileImage(@AuthenticationPrincipal CustomOAuth2User user);
}
