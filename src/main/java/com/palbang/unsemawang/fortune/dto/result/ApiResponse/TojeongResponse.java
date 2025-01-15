package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "토정비결 Response DTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TojeongResponse {

	// 현재 나의 운 분석
	@Schema(required = true)
	private CommonResponse currentLuckAnalysis;

	// 올해의 운세
	@Schema(required = true)
	private CommonResponse thisYearLuck;

	// 토정비결
	@Schema(required = true)
	private CommonResponse tojeongSecret;

	// 재물
	@Schema(required = true)
	private CommonResponse wealth;

	// 타고난 성품
	@Schema(required = true)
	private CommonResponse natureCharacter;

	// 현재 지켜야 할 처세
	@Schema(required = true)
	private CommonResponse currentBehavior;

	// 현재 대인 관계
	@Schema(required = true)
	private CommonResponse currentHumanRelationship;

	// 피해야 할 상대
	@Schema(required = true)
	private CommonResponse avoidPeople;
}