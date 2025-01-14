package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "인생풀이 Response DTO")
public class InsaengResponse {
	// 총평
	@Schema(required = true)
	private CommonResponse total;

	// 궁합
	@Schema(required = true)
	private CommonResponse gunghap;

	// 결혼 관련 정보
	@Schema(required = true)
	private CommonResponse marryGung;

	// 행운
	@Schema(required = true)
	private CommonResponse luck;

	// 별자리
	@Schema(required = true)
	private CommonResponse constellation;

	// 현재의 길흉사
	@Schema(required = true)
	private CommonResponse currentGoodAndBadNews;

	// 팔복궁
	@Schema(required = true)
	private CommonResponse eightStar;

	// 풍수로 보는 길흉
	@Schema(required = true)
	private CommonResponse goodAndBadByPungsu;

	// 천생연분
	@Schema(required = true)
	private CommonResponse soulMates;
}