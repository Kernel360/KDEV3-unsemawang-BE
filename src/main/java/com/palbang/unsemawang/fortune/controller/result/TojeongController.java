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
	public ResponseEntity<CommonResponse> getTojeongDetails(
		@RequestParam(required = false) Integer id,
		@RequestParam(required = false) String nameEn,
		@Valid @RequestBody FortuneApiRequest request) {

		if (id == null && nameEn == null) {
			// ResponseEntity의 body가 null이 될 수 있으므로 좀 더 명시적으로 작성
			return ResponseEntity.badRequest().build();
		}

		// id -> key 변환
		String key = (id != null) ? resolveKeyById(id) : nameEn.toLowerCase();

		try {
			// 서비스 호출: CommonResponse 반환
			CommonResponse response = tojeongService.getTojeongDetailByKey(request, key);
			return ResponseEntity.ok(response); // CommonResponse 반환
		} catch (IllegalArgumentException ex) {
			// 오류 메시지 전달
			return ResponseEntity.badRequest().body(new CommonResponse("Invalid Key", ex.getMessage(), null));
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