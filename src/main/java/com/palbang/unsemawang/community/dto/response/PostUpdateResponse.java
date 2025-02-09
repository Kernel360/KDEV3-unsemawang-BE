package com.palbang.unsemawang.community.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostUpdateResponse {

	@Schema(description = "수정된 게시글 ID", required = true, example = "1")
	private Long postId;

	public static PostUpdateResponse of(Long postId) {
		return PostUpdateResponse.builder()
			.postId(postId)
			.build();
	}
}
