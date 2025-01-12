package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.TojeongResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.TojeongService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tojeong")
public class TojeongController {

	private final TojeongService tojeongseService;

	@Operation(summary = "토정비결 API")
	@PostMapping
	public ResponseEntity<TojeongResponse> TojeongApiHandler(@RequestBody FortuneApiRequest request) {
		TojeongResponse response = tojeongseService.getTojeongResult(request);
		return ResponseEntity.ok(response);
	}
}