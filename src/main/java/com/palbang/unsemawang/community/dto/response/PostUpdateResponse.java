package com.palbang.unsemawang.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostUpdateResponse {

	private Long postId;

	public static PostUpdateResponse of(Long postId) {
		return PostUpdateResponse.builder()
			.postId(postId)
			.build();
	}
}
