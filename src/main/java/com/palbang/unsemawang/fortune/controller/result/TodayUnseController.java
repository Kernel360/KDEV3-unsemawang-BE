package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.TodayUnseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.TodayUnseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todayunse")
public class TodayUnseController {

	private final TodayUnseService todayunseService;

	@Operation(summary = "오늘의 운세 API")
	@PostMapping
	public ResponseEntity<TodayUnseResponse> TodayUnseApiHandler(@RequestBody FortuneApiRequest request) {
		TodayUnseResponse response = todayunseService.getTodayUnseResult(request);
		return ResponseEntity.ok(response);
	}
}