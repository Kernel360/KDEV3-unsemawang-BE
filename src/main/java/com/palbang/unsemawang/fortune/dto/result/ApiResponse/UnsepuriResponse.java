package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "현재 운세 풀이 Response DTO")
public class UnsepuriResponse {

	// 피해야 할 상대
	@Schema(required = true)
	private CommonResponse avoidPeople;

	// 현재 운세 풀이
	@Schema(required = true)
	private CommonResponse currentUnsepuri;

	// 행운의 요소
	@Schema(required = true)
	private CommonResponse luckElement;
}