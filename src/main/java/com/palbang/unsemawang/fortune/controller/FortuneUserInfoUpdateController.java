package com.palbang.unsemawang.fortune.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoUpdateRequest;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoUpdateResponse;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoUpdateService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "사주 정보")
@RestController
@RequestMapping("/fortune-users")
@RequiredArgsConstructor
public class FortuneUserInfoUpdateController {
	private final FortuneUserInfoUpdateService updateService;

	@Operation(
		summary = "사주 정보 수정",
		description = "특정 회원의 사주 정보를 수정한다.",
		responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Error", responseCode = "400")
		}
	)
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FortuneUserInfoUpdateResponse> updateFortuneUserInfo(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@RequestBody @Valid FortuneInfoUpdateRequest requestDto) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		requestDto.setMemberId(auth.getId());

		FortuneUserInfoUpdateResponse response = updateService.updateFortuneUserInfo(requestDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}
}