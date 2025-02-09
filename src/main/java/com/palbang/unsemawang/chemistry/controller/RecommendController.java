package com.palbang.unsemawang.chemistry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.palbang.unsemawang.chemistry.dto.response.ChemistryRecommendResponse;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "궁합 매칭")
public interface RecommendController {

	@Operation(
		description = "오행 기반 궁합이 잘맞는 회원 추천 리스트를 조회합니다",
		summary = "오행 기반 매칭 결과 조회"
	)
	@ApiResponse(responseCode = "200", description = "궁합 좋은 회원 추천 리스트 조회 성공")
	ResponseEntity<List<ChemistryRecommendResponse>> recommendChemistryMember(
		CustomOAuth2User user
	);
}
