package com.palbang.unsemawang.chemistry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chemistry.dto.response.ChemistryRecommendResponse;
import com.palbang.unsemawang.chemistry.service.RecommendService;
import com.palbang.unsemawang.chemistry.service.TotalCalculationService;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chemistry")
@RequiredArgsConstructor
public class RecommendControllerImpl implements RecommendController {

	private final RecommendService recommendService;
	private final TotalCalculationService totalCalculationService;

	@GetMapping("/recommendations")
	@Override
	public ResponseEntity<List<ChemistryRecommendResponse>> recommendChemistryMember(
		@AuthenticationPrincipal CustomOAuth2User auth
	) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 모든 사용자에 대한 궁합 점수 DB에 저장
		totalCalculationService.calculateAndSaveChemistryScores();

		List<ChemistryRecommendResponse> recommendedMemberList = recommendService.getTop5Matches(auth.getId());

		return ResponseEntity.ok(recommendedMemberList);
	}
}
