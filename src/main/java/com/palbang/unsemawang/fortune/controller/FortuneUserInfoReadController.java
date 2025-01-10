package com.palbang.unsemawang.fortune.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoReadResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoReadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Tag(name = "사주 정보")
@RestController
@RequestMapping("/fortune-users")
@RequiredArgsConstructor
public class FortuneUserInfoReadController {
	private final FortuneUserInfoReadService readService;

	@Operation(
		summary = "사주 정보 조회",
		description = "본인을 포함한 사중 정보를 조회한다",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<List<FortuneUserInfoReadResponseDto>>> getUserInfoList(
		@RequestParam @NotBlank(message = "memberId는 필수 값입니다.") String memberId) {

		List<FortuneUserInfoReadResponseDto> list = readService.fortuneInfoListRead(memberId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(
				Response.success(
					ResponseCode.SUCCESS_SEARCH,
					list
				)
			);
	}
}


