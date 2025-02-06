package com.palbang.unsemawang.chemistry.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChemistryRecommendResponse {

	@Schema(required = true, description = "나와의 궁합 매칭 점수")
	private Integer score;

	@Schema(required = true, description = "상대방의 오행")
	private char fiveElementCn;

	@Schema(required = true, description = "상대방 닉네임")
	private String nickname;

	@Schema(required = true, description = "상대방 프로필 이미지 url")
	private String profileImageUrl;

	@Schema(required = true, description = "상대방 프로필 소개")
	private String profileBio;

	@Schema(required = true, description = "상대방 성별")
	private char sex;

	@Schema(required = false, description = "상대방 마지막 로그인 날짜")
	private LocalDate lastActiveDate;
}
