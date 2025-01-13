package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.SinsuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sinsu")
public class SinsuController {

	private final SinsuService sinsuService;

	@Operation(summary = "새해 신수 API")
	@PostMapping
	public ResponseEntity<SinsuResponse> SinsuApiHandler(@RequestBody FortuneApiRequest request) {
		SinsuResponse response = sinsuService.getSinsuResult(request);
		return ResponseEntity.ok(response);
	}
}