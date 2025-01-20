package com.palbang.unsemawang.community.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostDetailService {

	private final PostRepository postRepository;

	@Transactional
	public PostDetailResponse getPostDetail(String memberId, Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		// 비공개 접근 권한 확인
		if (!post.getIsVisible() && !post.getMember().getId().equals(memberId)) {
			throw new GeneralException(ResponseCode.FORBIDDEN); // 적절한 응답 코드 사용
		}

		// DB 레벨에서 조회수 증가
		incrementViewCount(postId);

		return toResponseDto(post);
	}

	// 조회수 증가 - DB 레벨 처리
	private void incrementViewCount(Long postId) {
		postRepository.incrementViewCount(postId);
	}

	// Dto 컨버터
	private PostDetailResponse toResponseDto(Post post) {
		return PostDetailResponse.builder()
			.id(post.getId())
			.memberId(post.getMember().getId())
			.title(post.getTitle())
			.content(post.getContent())
			.nickname(post.getIsAnonymous() ? "익명" : post.getMember().getNickname())
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