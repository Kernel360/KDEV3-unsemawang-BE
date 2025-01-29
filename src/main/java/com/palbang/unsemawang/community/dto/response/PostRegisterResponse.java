package com.palbang.unsemawang.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostRegisterResponse {

	private Long postId;

	public static PostRegisterResponse of(Long postId) {
		return PostRegisterResponse.builder()
			.postId(postId)
			.build();
	}
}
