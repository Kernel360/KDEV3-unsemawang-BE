package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.GunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.GunghapApiRequest;
import com.palbang.unsemawang.fortune.service.result.GunghapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gunghap")
public class GunghapController {

	private final GunghapService gunghapService;

	@Operation(summary = "궁합 API")
	@PostMapping
	public ResponseEntity<GunghapResponse> GunghapApiHandler(@Valid @RequestBody GunghapApiRequest request) {
		// 서비스 호출 후 결과를 처리
		GunghapResponse response = gunghapService.getGunghapResult(request);

		return ResponseEntity.ok(response);
	}
}