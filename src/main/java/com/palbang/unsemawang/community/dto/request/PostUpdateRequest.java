package com.palbang.unsemawang.community.dto.request;

import com.palbang.unsemawang.community.constant.CommunityCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

	@NotNull(message = "게시글 ID를 입력해 주세요")
	private Long postId;

	@NotBlank(message = "제목을 입력해 주세요")
	@Size(min = 1, max = 30, message = "제목은 30자 이내여야 합니다")
	private String title;

	@NotBlank(message = "내용을 입력해 주세요")
	@Size(min = 1, max = 1000, message = "내용은 1,000자 이내여야 합니다")
	private String content;

	@NotNull(message = "카테고리를 입력해 주세요")
	private CommunityCategory category;

}
