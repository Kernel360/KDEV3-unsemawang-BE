package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentsReadResponse {
	@Schema(required = true)
	private Long commentId;

	@Schema(required = true)
	private Long postId;

	@Schema(required = true)
	private String communityCategory;

	@Schema(required = true)
	private String postTitle;

	@Schema(required = true)
	private String commentContent;

	@Schema(required = true)
	private LocalDateTime registeredAt;
}
