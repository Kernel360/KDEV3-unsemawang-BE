package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReadResponse {
	@Schema(required = true)
	private Long commentId;

	@Schema(required = false)
	private Boolean isMyComment;

	@Schema(required = true)
	private String nickname; // 익명 게시판 -> 익명 이름, 자유 게시판 -> 사용자 별명

	@Schema(required = true)
	private String content;

	@Schema(required = true)
	private LocalDateTime registeredAt;

	@Schema(required = true)
	private int repliesCount;

	@Schema(required = true)
	private String imageUrl;

	@Schema(required = false)
	private List<CommentReadResponse> replies;
}
