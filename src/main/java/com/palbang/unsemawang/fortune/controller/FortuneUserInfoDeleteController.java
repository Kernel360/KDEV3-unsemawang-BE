package com.palbang.unsemawang.fortune.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoDeleteResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoDeleteService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Tag(name = "사주 정보")
@RestController
@RequestMapping("/fortune-users")
@RequiredArgsConstructor
public class FortuneUserInfoDeleteController {
	private final FortuneUserInfoDeleteService deleteService;

	@Operation(
		summary = "사주 정보 삭제",
		description = "특정 회원의 사주 정보를 삭제한다.",
		responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Error", responseCode = "400")
		}
	)
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FortuneUserInfoDeleteResponseDto> deleteFortuneUserInfo(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestParam @NotNull(message = "relationId는 필수 값입니다.") Long relationId) {

		FortuneUserInfoDeleteResponseDto response = deleteService.deleteFortuneUserInfo(auth.getId(), relationId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}
}
