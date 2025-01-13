package com.palbang.unsemawang.community.constant;

import lombok.Getter;

@Getter
public enum ReportReason {
	SPAM("스팸 또는 광고"),
	ABUSE("욕설 및 비방"),
	HATE_SPEECH("혐오 표현"),
	INAPPROPRIATE_CONTENT("음란물 또는 부적절한 콘텐츠"),
	VIOLENCE("폭력 또는 유해 콘텐츠"),
	FALSE_INFORMATION("허위 정보"),
	PRIVACY_VIOLATION("사생활 침해"),
	COPYRIGHT_INFRINGEMENT("저작권 침해"),
	WRONG_CATEGORY("부적절한 카테고리 또는 주제"),
	OTHER("기타");

	private final String description;

	ReportReason(String description) {
		this.description = description;
	}
}
