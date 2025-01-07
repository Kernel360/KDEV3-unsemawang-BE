package com.palbang.unsemawang.fortune.dto.response;

import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.member.entity.FortuneCategory;

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
public class ContentSummery {
	private Long id;
	private Long fortuneCategoryId;
	private String name;
	private String description;

	public static ContentSummery of(FortuneContent fortuneContent) {
		FortuneCategory fortuneCategory = fortuneContent.getFortuneCategory();

		return ContentSummery.builder()
			.id(fortuneContent.getId())
			.fortuneCategoryId(fortuneCategory != null ? fortuneCategory.getId() : null)
			.name(fortuneContent.getFortuneContentName())
			.description(fortuneContent.getDescription())
			.build();
	}
}
