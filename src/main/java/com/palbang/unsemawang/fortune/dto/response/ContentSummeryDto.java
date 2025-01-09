package com.palbang.unsemawang.fortune.dto.response;

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
public class ContentSummeryDto {
	private Long id;
	private String nameEn;
	private String nameKo;
	private String shortDescription;
	private String thumbnailUrl;

	public static ContentSummeryDto of(FortuneContent fortuneContent) {
		return ContentSummeryDto.builder()
			.id(fortuneContent.getId())
			.nameEn(fortuneContent.getNameEn())
			.nameKo(fortuneContent.getNameKo())
			.shortDescription(fortuneContent.getShortDescription())
			.build();
	}
}
