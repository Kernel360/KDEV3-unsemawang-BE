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
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unsepuri")
public class UnsepuriController {

	private final UnsepuriService unsepuriseService;

	@Operation(summary = "운세 풀이 API")
	@PostMapping
	public ResponseEntity<UnsepuriResponse> UnsepuriApiHandler(@RequestBody FortuneApiRequest request) {
		UnsepuriResponse response = unsepuriseService.getUnsepuriResult(request);
		return ResponseEntity.ok(response);
	}
}
