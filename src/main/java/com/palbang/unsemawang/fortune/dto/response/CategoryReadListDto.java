package com.palbang.unsemawang.fortune.dto.response;

import java.util.List;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReadListDto {
	private Long id;
	private String name;

	// 운세 카테고리 엔티티를 CategorySummery 객체로 변경
	public static CategoryReadListDto of(FortuneCategory fortuneCategory) {
		return CategoryReadListDto.builder()
			.id(fortuneCategory.getId())
			.name(fortuneCategory.getName())
			.build();
	}

	// 운세 카테고리 엔티티 리스트를  CategorySummeryDto 리스트로 변경
	public static List<CategoryReadListDto> of(List<FortuneCategory> fortuneCategoryList) {
		// 운세 카테고리 엔티티 리스트를 CategorySummery 리스트로 변형
		return fortuneCategoryList.stream()
			.map(CategoryReadListDto::of)
			.toList();
	}
}
