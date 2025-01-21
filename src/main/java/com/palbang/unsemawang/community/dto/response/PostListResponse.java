package com.palbang.unsemawang.community.dto.response;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.entity.Post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListResponse {

	private Long cursorId;

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

	// @Schema(description = "게시글 이미지 URL", required = false)
	// private String imageUrl;

	public static PostListResponse fromEntity(Post post, FileService fileService) {
		// String imageUrl = getImageUrl(post, fileService);

		return PostListResponse.builder()
			.cursorId(post.getId())
			.id(post.getId())
			.title(post.getTitle())
			.snippet(post.getContent().substring(0, Math.min(post.getContent().length(), 100)))
			.viewCount(post.getViewCount())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.nickname(post.getIsAnonymous() ? "익명" : post.getMember().getNickname())
			.communityCategory(post.getCommunityCategory())
			.registeredAt(post.getRegisteredAt())
			.updatedAt(post.getUpdatedAt())
			// .imageUrl(imageUrl)
			.build();
	}

	// private static String getImageUrl(Post post, FileService fileService) {
	// 	FileRequest fileRequest = FileRequest.of(FileReferenceType.COMMUNITY_BOARD, post.getId().toString());
	//
	// 	// FileService를 통해 파일 URL 리스트 가져옴
	// 	List<String> fileUrls = fileService.getFileUrls(fileRequest);
	//
	// 	// 썸네일 URL로 첫 번째 파일 선택 (없을 경우 기본 이미지 반환)
	// 	return fileUrls.isEmpty() ? "https://default-image-url.com/default.jpg" : fileUrls.get(0);
	// }
}