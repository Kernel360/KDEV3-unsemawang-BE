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

	@Schema(description = "대분류 카테고리명", required = false)
	private String categoryName;

	public SearchRequest(String keyword, String categoryName) {
		this.keyword = keyword == null || keyword.isBlank() ? "" : keyword;
		this.categoryName = categoryName;
	}
}