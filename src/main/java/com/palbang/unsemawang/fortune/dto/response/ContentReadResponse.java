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
	private ContentDetailDto content;

	public static ContentReadResponse of(FortuneContent fortuneContent) {
		return new ContentReadResponse(ContentDetailDto.of(fortuneContent));
	}
}
