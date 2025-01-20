package com.palbang.unsemawang.fortune.controller.result;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
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

	@Operation(summary = "토정비결 운세 항목 조회 API")
	@PostMapping
	public ResponseEntity<?> getTojeongDetails( // 메서드 명 수정
		@RequestParam(required = false) Integer id,
		@RequestParam(required = false) String nameEn,
		@Valid @RequestBody FortuneApiRequest request) {

		if (id == null && nameEn == null) {
			return ResponseEntity.badRequest().body("id 또는 nameEn 중 하나는 필수입니다.");
		}

		// id로 key 추출
		String key = (id != null) ? resolveKeyById(id) : nameEn.toLowerCase();

		// 서비스 호출 후 특정 key에 대한 Map 반환
		try {
			Map<String, CommonResponse> response = tojeongService.getTojeongDetailByKey(request, key);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	private String resolveKeyById(int id) {
		Map<Integer, String> idToKeyMap = Map.of(
			1, "currentluckanalysis",
			2, "thisyearluck",
			3, "tojeongsecret",
			4, "wealth",
			5, "naturecharacter",
			6, "currentbehavior",
			7, "currenthumanrelationship",
			8, "avoidpeople"
		);
		return idToKeyMap.get(id);
	}
}