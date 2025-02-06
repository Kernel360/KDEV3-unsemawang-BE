package com.palbang.unsemawang.chemistry.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class MatchingResponse {
	private Integer score;
	private char fiveElementCn;
	private String nickname;
	private String profileImageUrl;
	private String profileBio;
	private char sex;
	private LocalDate lastActiveDate;
}
