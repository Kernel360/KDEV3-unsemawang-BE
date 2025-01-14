package com.palbang.unsemawang.community.dto.request;

import com.palbang.unsemawang.community.constant.CommunityCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.palbang.unsemawang.community.entity.Post}
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRegisterRequest {

	@Size(min = 1, max = 30)
	@NotBlank
	private String title;

	@Size(min = 1, max = 1000)
	@NotBlank
	private String content;

	@NotNull
	private CommunityCategory category;

	@NotNull
	private Boolean isAnonymous;

}