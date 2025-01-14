package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.UnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.UnsepuriService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/unsepuri")
public class UnsepuriController {

	private final UnsepuriService unsepuriService;

	@Operation(summary = "운세 풀이 API")
	@PostMapping
	public ResponseEntity<UnsepuriResponse> UnsepuriApiHandler(@Valid @RequestBody FortuneApiRequest request) {
		log.info("Received request for Unsepuri API: {}", request);

		// 서비스 호출 후 결과를 처리
		UnsepuriResponse response = unsepuriService.getUnsepuriResult(request);

		// 응답 데이터 로깅
		log.info("Responding with data for Unsepuri API: {}", response);

		// 성공적인 응답 반환
		return ResponseEntity.ok(response);
	}
}
