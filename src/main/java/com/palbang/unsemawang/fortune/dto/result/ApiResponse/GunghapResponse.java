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
	private InnerGunghap innerGunghap;

	// 남여 운명
	@Schema(required = true)
	private ManFemaleFate manFemaleFate;

	// 겉궁합
	@Schema(required = true)
	private OuterGunghap outerGunghap;

	// 타입 분석
	@Schema(required = true)
	private TypeAnalysis typeAnalysis;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InnerGunghap {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ManFemaleFate {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OuterGunghap {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TypeAnalysis {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}
}