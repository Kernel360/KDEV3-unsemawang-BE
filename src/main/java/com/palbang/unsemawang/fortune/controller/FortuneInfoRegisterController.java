package com.palbang.unsemawang.fortune.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequestDto;
import com.palbang.unsemawang.fortune.dto.response.FortuneInfoRegisterResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneInfoRegisterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "사주 정보")
@RestController
@RequestMapping("/fortune-users")
@RequiredArgsConstructor
public class FortuneInfoRegisterController {
	private final FortuneInfoRegisterService registerService;

	@Operation(
		summary = "사주 정보 등록",
		description = "본인을 포함한 사중 정보를 등록한다",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FortuneInfoRegisterResponseDto> registerFortuneUser(
		@RequestBody FortuneInfoRegisterRequestDto requestDto) {

		FortuneInfoRegisterResponseDto responseDto = registerService.registerFortuneInfo(requestDto);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(
				responseDto
			);
	}
}
