package com.palbang.unsemawang.fortune.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.palbang.unsemawang.fortune.entity.FortuneContent;

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
	Long id;
	String nameEn;
	String nameKo;
	String shortDescription;
	String longDescription;
	String thumbnailUrl;
	String path;
	LocalDateTime registeredAt;
	LocalDateTime updatedAt;
	Boolean isVisible;
	Boolean isDeleted;
	LocalDateTime deletedAt;

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