package com.palbang.unsemawang.fortune.dto.response;

import java.util.List;

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
public class ContentReadListDto {
	private Long id;
	private String nameEn;
	private String nameKo;
	private String shortDescription;
	private String thumbnailUrl;

	public static ContentReadListDto of(FortuneContent fortuneContent) {
		return ContentReadListDto.builder()
			.id(fortuneContent.getId())
			.nameEn(fortuneContent.getNameEn())
			.nameKo(fortuneContent.getNameKo())
			.shortDescription(fortuneContent.getShortDescription())
			.build();
	}

	public static List<ContentReadListDto> of(List<FortuneContent> fortuneContentList) {
		return fortuneContentList.stream()
			.map(ContentReadListDto::of)
			.toList();
	}
}
