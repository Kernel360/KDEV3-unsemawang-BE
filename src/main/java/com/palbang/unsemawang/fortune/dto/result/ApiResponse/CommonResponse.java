package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommonResponse {
	@Schema(required = true)
	private String label;
	@Schema(required = true)
	private String value;
	@Schema(required = false)
	private List<CommonResponse> children;
}
