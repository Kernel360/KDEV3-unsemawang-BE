package com.palbang.unsemawang.fortune.dto.request;

import lombok.Builder;

@Builder
public record SearchRequest(String keyword, String order) {
	public SearchRequest(String keyword) {
		// 키워드가 null이거나 공백이면 "" 으로 변경
		this(keyword == null || keyword.isBlank() ? "" : keyword, null);
	}
}
