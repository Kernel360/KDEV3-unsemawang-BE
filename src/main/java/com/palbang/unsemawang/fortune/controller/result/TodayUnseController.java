package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.TodayUnseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todayunse")
public class TodayUnseController {

	private final TodayUnseService todayUnseService;

	@Operation(summary = "오늘의 운세 API")
	@PostMapping
	public ResponseEntity<CommonResponse> getTodayUnseDetail(
		@RequestParam(required = false) String nameEn,
		@Valid @RequestBody FortuneApiRequest request) {

		String key = nameEn.toLowerCase();

		CommonResponse response = todayUnseService.getTodayUnseDetail(request, key);
		return ResponseEntity.ok(response);
	}
}
