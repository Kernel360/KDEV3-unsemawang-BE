package com.palbang.unsemawang.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegisterRequest {
	@Schema(required = false, description = "부모 댓글 ID", defaultValue = "null")
	private Long parentCommentId; // 부모 댓글 ID (대댓글일 경우 필요)

	@Schema(required = true, description = "댓글 내용")
	@NotBlank
	@Size(min = 1, max = 250)
	private String content;
}
