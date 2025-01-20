package com.palbang.unsemawang.fortune.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoReadResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoReadService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "본인 사주 정보")
@RestController
@RequestMapping("/fortune-self")
@RequiredArgsConstructor
public class FortuneSelfInfoController {
	private final FortuneUserInfoReadService readService;

	@Operation(
		summary = "본인 사주 정보 조회",
		description = "본인의 사주 정보 조회",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FortuneUserInfoReadResponseDto> getUserSelfInfo(
		@AuthenticationPrincipal CustomOAuth2User auth) {

		FortuneUserInfoReadResponseDto fortuneUserInfoReadResponseDto = readService.getFortuneUserSelf(auth.getId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(
				fortuneUserInfoReadResponseDto
			);
	}
}
