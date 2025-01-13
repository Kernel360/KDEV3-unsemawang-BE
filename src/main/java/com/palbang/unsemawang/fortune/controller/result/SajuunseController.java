package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.SajuunseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sajuunse")
public class SajuunseController {

	private final SajuunseService sajuunseService;

	@Operation(summary = "사주 운세 API")
	@PostMapping
	public ResponseEntity<SajuunseResponse> SajuunseApiHandler(@RequestBody FortuneApiRequest request) {
		SajuunseResponse response = sajuunseService.getSajuunseResult(request);
		return ResponseEntity.ok(response);
	}
}