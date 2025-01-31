package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;

import com.palbang.unsemawang.community.constant.CommunityCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPostReadResponse {
	@Schema(required = true)
	private Long postId;

	@Schema(required = true)
	private String postTitle;

	@Schema(required = true)
	private CommunityCategory communityCategory;

	@Schema(required = true)
	private LocalDateTime registeredAt;

	@Schema(required = true)
	private int viewCount;

	@Schema(required = true)
	private int likeCount;

	@Schema(required = true)
	private int commentCount;

	@Schema(required = true)
	private String imageUrl;
}
