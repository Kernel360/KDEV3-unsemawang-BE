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
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/insaeng")
public class InsaengController {

	private final InsaengService insaengService;

	@Operation(summary = "인생 풀이 API")
	@PostMapping
	public ResponseEntity<InsaengResponse> InsaengApiHandler(@RequestBody FortuneApiRequest request) {
		InsaengResponse response = insaengService.getInsaengResult(request);
		return ResponseEntity.ok(response);
	}
}