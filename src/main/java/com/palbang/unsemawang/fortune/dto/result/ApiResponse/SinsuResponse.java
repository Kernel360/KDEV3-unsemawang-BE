package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "새해 신수 Response DTO")
public class SinsuResponse {

	// 올해의 행운
	@Schema(required = true)
	private CommonResponse thisYearLuck;
}