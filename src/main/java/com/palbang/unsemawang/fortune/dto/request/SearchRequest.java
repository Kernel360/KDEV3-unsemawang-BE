package com.palbang.unsemawang.fortune.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SearchRequest {

	@Schema(description = "검색할 컨텐츠명 키워드", required = false)
	private String keyword;

	public SearchRequest(String keyword) {
		// 키워드가 null이거나 공백이면 "" 으로 변경
		this.keyword = keyword == null || keyword.isBlank() ? "" : keyword;
	}
}
