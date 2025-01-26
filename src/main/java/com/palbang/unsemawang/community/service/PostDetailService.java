package com.palbang.unsemawang.community.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostDetailService {

	private final FileService fileService;
	private final PostRepository postRepository;

	@Transactional(readOnly = true)
	public PostDetailResponse getPostDetail(String memberId, Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		// 비공개 접근 권한 확인
		if (!post.getIsVisible() && !post.getMember().getId().equals(memberId)) {
			throw new GeneralException(ResponseCode.FORBIDDEN);
		}

		// 삭제 처리된 글 조회 제한
		if (post.getIsDeleted()) {
			throw new GeneralException(ResponseCode.NOT_EXIST_POST);
		}

		// 게시글에 포함된 이미지 리스트 조회
		List<String> imageUrls = fileService.getPostImgUrls(post.getId());

		// 작성자의 프로필 이미지 조회
		String profileImage = post.getIsAnonymous() ? null : fileService.getProfileImgUrl(memberId);

		return toResponseDto(post, imageUrls, profileImage);
	}

	// 조회수 증가 - DB 레벨 처리
	@Transactional
	public void incrementViewCount(Long postId) {
		postRepository.incrementViewCount(postId);
	}

	// Dto 컨버터
	private PostDetailResponse toResponseDto(Post post, List<String> imageUrls, String profileImageUrl) {
		return PostDetailResponse.builder()
			.id(post.getId())
			.memberId(post.getMember().getId())
			.title(post.getTitle())
			.content(post.getContent())
			.nickname(post.getIsAnonymous() ? "익명" : post.getMember().getNickname())
			.profileImageUrl(profileImageUrl)
			.postImageUrls(imageUrls)
			.isAnonymous(post.getIsAnonymous())
			.isVisible(post.getIsVisible())
			.viewCount(post.getViewCount())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.communityCategory(post.getCommunityCategory())
			.registeredAt(post.getRegisteredAt())
			.updatedAt(post.getUpdatedAt())
			.build();
	}
}