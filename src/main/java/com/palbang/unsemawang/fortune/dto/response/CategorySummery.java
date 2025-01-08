package com.palbang.unsemawang.fortune.dto.response;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummery {
	private Long id;
	private String name;

	// 운세 카테고리 엔티티를 CategorySummery 객체로 변경
	public static CategorySummery of(FortuneCategory fortuneCategory) {
		return CategorySummery.builder()
			.id(fortuneCategory.getId())
			.name(fortuneCategory.getName())
			.build();
	}

}
