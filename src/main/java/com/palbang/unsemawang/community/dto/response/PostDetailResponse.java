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
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {

	@Schema(description = "게시글 ID", required = true, example = "1")
	private Long id;

	@Schema(description = "게시글 제목", required = true, example = "게시글 제목")
	private String title;

	@Schema(description = "게시글 본문 내용", required = true, example = "게시글 내용")
	private String content;

	@Schema(description = "작성자 이름 (익명일 경우 '익명')", required = true, example = "작성자 이름")
	private String author;

	@Schema(description = "익명 여부", required = true, example = "false")
	private Boolean isAnonymous;

	@Schema(description = "조회수", required = true, example = "123")
	private Integer viewCount;

	@Schema(description = "좋아요 수", required = true, example = "45")
	private Integer likeCount;

	@Schema(description = "댓글 수", required = true, example = "10")
	private Integer commentCount;

	@Schema(description = "게시판 카테고리", required = true, example = "자유 게시판")
	private CommunityCategory communityCategory;

	@Schema(description = "게시글 등록 시각", required = true, example = "2023-12-01T10:00:00")
	private LocalDateTime postedAt;

	@Schema(description = "게시글 마지막 수정 시각", required = true, example = "2023-12-02T10:00:00")
	private LocalDateTime lastUpdatedAt;
}