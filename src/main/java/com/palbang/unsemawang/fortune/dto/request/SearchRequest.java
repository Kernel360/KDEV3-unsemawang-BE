package com.palbang.unsemawang.fortune.dto.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SearchRequest {

	private String keyword;

	public SearchRequest(String keyword) {
		// 키워드가 null이거나 공백이면 "" 으로 변경
		this.keyword = keyword == null || keyword.isBlank() ? "" : keyword;
	}
}
