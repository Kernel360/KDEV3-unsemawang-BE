package com.palbang.unsemawang.fortune.dto.response;

import java.util.List;

import com.palbang.unsemawang.fortune.entity.FortuneContent;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "운세 컨텐츠 ID", required = true)
	private Long id;

	@Schema(description = "운세 컨텐츠 영어표기명", required = true)
	private String nameEn;

	@Schema(description = "운세 컨텐츠 한글표기명", required = true)
	private String nameKo;

	@Schema(description = "운세 컨텐츠 짧은 설명", required = false)
	private String shortDescription;

	@Schema(description = "운세 컨텐츠 썸네일 URL", required = false)
	private String thumbnailUrl;

	public static ContentReadListDto of(FortuneContent fortuneContent) {
		return ContentReadListDto.builder()
			.id(fortuneContent.getId())
			.nameEn(fortuneContent.getNameEn())
			.nameKo(fortuneContent.getNameKo())
			.shortDescription(fortuneContent.getShortDescription())
			.thumbnailUrl(fortuneContent.getThumbnailUrl())
			.build();
	}

	public static List<ContentReadListDto> of(List<FortuneContent> fortuneContentList) {
		return fortuneContentList.stream()
			.map(ContentReadListDto::of)
			.toList();
	}
}
