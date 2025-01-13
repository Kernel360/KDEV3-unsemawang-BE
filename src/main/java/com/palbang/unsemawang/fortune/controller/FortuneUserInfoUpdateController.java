package com.palbang.unsemawang.fortune.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.request.UpdateRequest;
import com.palbang.unsemawang.fortune.dto.response.UpdateResponse;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoUpdateService;

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
	public ResponseEntity<UpdateResponse> updateFortuneUserInfo(
		@RequestBody @Valid UpdateRequest requestDto) {

		UpdateResponse response = updateService.updateFortuneUserInfo(requestDto);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}
}