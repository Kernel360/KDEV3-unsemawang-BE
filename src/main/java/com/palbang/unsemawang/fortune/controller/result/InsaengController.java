package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.InsaengResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.InsaengService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/insaeng")
public class InsaengController {

	private final InsaengService insaengService;

	@Operation(summary = "인생 풀이 API")
	@PostMapping
	public ResponseEntity<InsaengResponse> InsaengApiHandler(@Valid @RequestBody FortuneApiRequest request) {
		log.info("Received request for Insaeng API: {}", request);

		// 서비스 호출 후 결과를 처리
		InsaengResponse response = insaengService.getInsaengResult(request);

		// 응답 데이터 로깅
		log.info("Responding with data for Insaeng API: {}", response);

		// 성공적인 응답 반환
		return ResponseEntity.ok(response);
	}
}