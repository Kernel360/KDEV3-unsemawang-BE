package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사주 운세 Response DTO")
public class SajuunseResponse {
	// 태어난 계절에 따른 운
	@Schema(required = true)
	private CommonResponse bornSeasonLuck;

	// 행운
	@Schema(required = true)
	private CommonResponse luck;

	// 타고난 성품
	@Schema(required = true)
	private CommonResponse natureCharacter;

	// 사회적 특성
	@Schema(required = true)
	private CommonResponse socialCharacter;

	// 사회적 성격
	@Schema(required = true)
	private CommonResponse socialPersonality;

	// 피해야 할 상대
	@Schema(required = true)
	private CommonResponse avoidPeople;

	// 현재 나의 운 분석
	@Schema(required = true)
	private CommonResponse currentLuckAnalysis;

}