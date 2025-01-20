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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tojeong")
public class TojeongController {

	private final TojeongService tojeongService;

	@Operation(summary = "토정비결 API")
	@PostMapping
	public ResponseEntity<TojeongResponse> TojeongApiHandler(@Valid @RequestBody FortuneApiRequest request) {
		// 서비스 호출 후 결과를 처리
		TojeongResponse response = tojeongService.getTojeongResult(request);

		return ResponseEntity.ok(response);
	}
}
