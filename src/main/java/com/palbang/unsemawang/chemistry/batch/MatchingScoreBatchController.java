package com.palbang.unsemawang.chemistry.batch;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class MatchingScoreBatchController {

	private final TotalCalculationService totalCalculationService;

	@Operation(
		description = "배치 돌리는 테스트용 컨트롤러, 무시해주세요.",
		summary = "테스트 배치 컨트롤러"
	)
	@GetMapping("/matching-score")
	public ResponseEntity<Void> batchMatchingScore() {

		totalCalculationService.runDailyChemistryScoreBatchAt4AM();

		return ResponseEntity.ok().build();
	}

	@Operation(
		description = "회원가입 완료 시 궁합 점수 계산 로직 실행",
		summary = "회원가입 후 로직 실행"
	)
	@GetMapping("/new-member-matching-score")
	public ResponseEntity<Void> calculateNewMemberMatchingScore(@AuthenticationPrincipal CustomOAuth2User auth) {

		totalCalculationService.calculateAndSaveChemistryScoresForNewMember(auth.getId());

		return ResponseEntity.ok().build();
	}
}
