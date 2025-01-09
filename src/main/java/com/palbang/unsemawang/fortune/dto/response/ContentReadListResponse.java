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
public class ContentReadListResponse {
	private List<ContentSummeryDto> contentList;

	public static ContentReadListResponse of(List<FortuneContent> fortuneContentList) {
		List<ContentSummeryDto> contentSummeryList =
			fortuneContentList.stream()
				.map(ContentSummeryDto::of)
				.toList();

		return ContentReadListResponse.builder()
			.contentList(contentSummeryList)
			.build();
	}
}
