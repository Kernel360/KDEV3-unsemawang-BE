package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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

	// @Schema(description = "작성자 ID", required = true, example = "UUID")
	// private String memberId;

	@Schema(description = "게시글 제목", required = true, example = "게시글 제목")
	private String title;

	@Schema(description = "게시글 본문 내용", required = true, example = "게시글 내용")
	private String content;

	@Schema(description = "작성자 닉네임 (익명일 경우 '익명')", required = true, example = "닉네임")
	private String nickname;

	@Schema(description = "작성자 프로필 사진 URL", required = false, example = "https://...")
	private String profileImageUrl;

	@Schema(description = "이미지 url 목록", required = true, example = "[\"https://...\" , ... ]")
	private List<String> postImageUrls;

	@Schema(description = "익명 여부(익명 O : true / 익명 X : false)", required = true, example = "false")
	private Boolean isAnonymous;

	@Schema(description = "공개 여부(공개 O : true / 공개 X : false)", required = true, example = "true")
	private Boolean isVisible;

	@Schema(description = "조회수", required = true, example = "123")
	private Integer viewCount;

	@Schema(description = "좋아요 수", required = true, example = "45")
	private Integer likeCount;

	@Schema(description = "댓글 수", required = true, example = "10")
	private Integer commentCount;

	@Schema(description = "게시판 카테고리", required = true, example = "자유 게시판")
	private CommunityCategory communityCategory;

	@Schema(description = "내가 작성한 게시글 여부", required = true, example = "true")
	private Boolean isMyPost;

	@Schema(description = "게시글 등록 시각", required = true, example = "2023-12-01T10:00:00")
	private LocalDateTime registeredAt;

	@Schema(description = "게시글 수정 시각", required = true, example = "2023-12-02T10:00:00")
	private LocalDateTime updatedAt;
}