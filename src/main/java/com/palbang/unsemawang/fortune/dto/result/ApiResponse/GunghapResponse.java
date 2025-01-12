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
	private InnerGunghap innerGunghap;

	// 남여 운명
	private ManFemaleFate manFemaleFate;

	// 겉궁합
	private OuterGunghap outerGunghap;

	// 타입 분석
	private TypeAnalysis typeAnalysis;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InnerGunghap {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ManFemaleFate {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OuterGunghap {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TypeAnalysis {
		private String label;
		private String value;
	}
}