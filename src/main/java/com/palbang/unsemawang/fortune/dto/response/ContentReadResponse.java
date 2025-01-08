package com.palbang.unsemawang.fortune.dto.response;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.entity.FortuneContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContentReadResponse {
	private Long id;
	private Long fortuneCategoryId;
	private String name;
	private String description;

	public static ContentReadResponse of(FortuneContent fortuneContent) {
		FortuneCategory fortuneCategory = fortuneContent.getFortuneCategory();

		return ContentReadResponse.builder()
			.id(fortuneContent.getId())
			.fortuneCategoryId(fortuneCategory != null ? fortuneCategory.getId() : null)
			.name(fortuneContent.getFortuneContentName())
			.description(fortuneContent.getDescription())
			.build();
	}
}
