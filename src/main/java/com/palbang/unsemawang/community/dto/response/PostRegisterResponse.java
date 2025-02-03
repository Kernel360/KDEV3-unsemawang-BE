package com.palbang.unsemawang.community.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostRegisterResponse {

	@Schema(description = "등록된 게시글 ID", required = true, example = "1")
	private Long postId;

	public static PostRegisterResponse of(Long postId) {
		return PostRegisterResponse.builder()
			.postId(postId)
			.build();
	}
}
