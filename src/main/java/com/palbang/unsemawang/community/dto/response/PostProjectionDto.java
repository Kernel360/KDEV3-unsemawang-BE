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
public class PostProjectionDto {

	@Schema(description = "게시글 ID", required = true, example = "1")
	private Long id;

	@Schema(description = "게시글 제목", required = true, example = "게시글 제목")
	private String title;

	@Schema(description = "게시글 요약(본문 중 일부)", required = true, example = "게시글 요약")
	private String snippet;

	@Schema(description = "게시글 조회수", required = true, example = "123")
	private Integer viewCount;

	@Schema(description = "게시글 좋아요 수", required = true, example = "10")
	private Integer likeCount;

	@Schema(description = "게시글 댓글 수 ", required = true, example = "10")
	private Integer commentCount;

	@Schema(description = "사용자 닉네임", required = true, example = "유저 닉네임")
	private String nickname;

	@Schema(description = "게시판 카테고리", required = true, example = "자유 게시판")
	private CommunityCategory communityCategory;

	@Schema(description = "게시글 등록 시각", required = true, example = "2023-12-01T10:00:00")
	private LocalDateTime registeredAt;

	@Schema(description = "게시글 수정 시각", required = true, example = "2023-12-02T10:00:00")
	private LocalDateTime updatedAt;

}