package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "오늘의 운세 Response DTO")
public class TodayUnseResponse {

	// 오행 분석
	@Schema(required = true)
	private CommonResponse luck;
}