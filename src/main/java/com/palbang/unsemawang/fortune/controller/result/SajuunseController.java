package com.palbang.unsemawang.fortune.controller.result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;
import com.palbang.unsemawang.fortune.service.result.SajuunseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "운세 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sajuunse")
public class SajuunseController {

	private final SajuunseService sajuunseService;

	@Operation(summary = "사주 운세 API")
	@PostMapping
	public ResponseEntity<Object> SajuunseApiHandler(
		@Valid @RequestBody FortuneApiRequest request,
		@RequestParam(required = false) String field) {

		log.info("Received request for Sajuunse API: {}", request);

		// 서비스 호출 후 결과를 처리
		SajuunseResponse response = sajuunseService.getSajuunseResult(request);

		// 응답 데이터 로깅
		log.info("Responding with data for Sajuunse API: {}", response);

		// 조건별 필드 반환
		switch (field != null ? field.toLowerCase() : "all") {
			case "bornseasonluck":
				return ResponseEntity.ok(response.getBornSeasonLuck());
			case "luck":
				return ResponseEntity.ok(response.getLuck());
			case "naturecharacter":
				return ResponseEntity.ok(response.getNatureCharacter());
			case "socialcharacter":
				return ResponseEntity.ok(response.getSocialCharacter());
			case "socialpersonality":
				return ResponseEntity.ok(response.getSocialPersonality());
			case "avoidpeople":
				return ResponseEntity.ok(response.getAvoidPeople());
			case "currentluckanalysis":
				return ResponseEntity.ok(response.getCurrentLuckAnalysis());
			default:
				// 쿼리 파라미터가 없거나 잘못된 경우, 전체 반환
				return ResponseEntity.ok(response);
		}
	}

	// 	default:
	// 		// 쿼리 파라미터가 없거나 잘못된 경우, 예외 발생
	// 		throw new IllegalArgumentException();
	// }

}