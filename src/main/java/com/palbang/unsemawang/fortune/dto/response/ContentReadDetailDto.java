package com.palbang.unsemawang.fortune.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.palbang.unsemawang.fortune.entity.FortuneContent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for {@link com.palbang.unsemawang.fortune.entity.FortuneContent}
 */

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContentReadDetailDto implements Serializable {

	@Schema(description = "운세 컨텐츠 ID", required = true)
	private Long id;

	@Schema(description = "운세 컨텐츠 영어표기명", required = true)
	private String nameEn;

	@Schema(description = "운세 컨텐츠 한글표기명", required = true)
	private String nameKo;

	@Schema(description = "운세 컨텐츠 짧은 설명", required = false)
	private String shortDescription;

	@Schema(description = "운세 컨텐츠 긴 설명", required = false)
	private String longDescription;

	@Schema(description = "운세 컨텐츠 썸네일 이미지", required = false)
	private String thumbnailUrl;

	@Schema(description = "운세 컨텐츠 api 경로", required = true)
	private String path;

	@Schema(description = "운세 컨텐츠 등록 일시", required = true)
	private LocalDateTime registeredAt;

	@Schema(description = "운세 컨텐츠 변경 일시", required = true)
	private LocalDateTime updatedAt;

	@Schema(description = "운세 컨텐츠 표시 여부", required = true)
	private Boolean isVisible;

	@Schema(description = "운세 컨텐츠 삭제 여부", required = true)
	private Boolean isDeleted;

	@Schema(description = "운세 컨텐츠 삭제 일시", required = false)
	private LocalDateTime deletedAt;

	public static ContentReadDetailDto of(FortuneContent fortuneContent) {
		return ContentReadDetailDto.builder()
			.id(fortuneContent.getId())
			.nameEn(fortuneContent.getNameEn())
			.nameKo(fortuneContent.getNameKo())
			.shortDescription(fortuneContent.getShortDescription())
			.longDescription(fortuneContent.getLongDescription())
			.thumbnailUrl(fortuneContent.getThumbnailUrl())
			.path(fortuneContent.getPath())
			.registeredAt(fortuneContent.getRegisteredAt())
			.updatedAt(fortuneContent.getUpdatedAt())
			.isVisible(fortuneContent.getIsVisible())
			.isDeleted(fortuneContent.getIsDeleted())
			.deletedAt(fortuneContent.getDeletedAt())
			.build();
	}
}