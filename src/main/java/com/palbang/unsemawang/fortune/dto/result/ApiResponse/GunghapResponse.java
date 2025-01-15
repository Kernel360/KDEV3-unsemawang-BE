package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "궁합 Response DTO")
public class GunghapResponse {
	// 속궁합
	@Schema(required = true)
	private CommonResponse innerGunghap;

	// 남여 운명
	@Schema(required = true)
	private CommonResponse manFemaleFate;

	// 겉궁합
	@Schema(required = true)
	private CommonResponse outerGunghap;

	// 타입 분석
	@Schema(required = true)
	private CommonResponse typeAnalysis;
}