package com.palbang.unsemawang.fortune.dto.response;

import java.util.List;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReadListResponse {
	private List<CategorySummery> fortuneCategoryList;

	// 운세 카테고리 엔티티 리스트를 CategoryReadListResponse DTO 객체로 변경
	public static CategoryReadListResponse of(List<FortuneCategory> fortuneCategoryList) {
		// 운세 카테고리 엔티티 리스트를 CategorySummery 리스트로 변형
		List<CategorySummery> categorySummeryList = fortuneCategoryList.stream()
			.map(CategorySummery::of)
			.toList();

		// CategorySummery 리스트로 필드가 초기화된 CategoryReadListResponse 객체 반환
		return CategoryReadListResponse.builder()
			.fortuneCategoryList(categorySummeryList)
			.build();
	}
}
