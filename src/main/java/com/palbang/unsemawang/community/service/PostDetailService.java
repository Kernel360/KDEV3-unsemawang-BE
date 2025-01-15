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
	public PostDetailResponse getPostDetail(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다."));

		handleViewCount(post);

		return toResponseDto(post);
	}

	// 조회수 증가
	private void handleViewCount(Post post) {
		post.increaseViewCount();
		postRepository.save(post);
	}

	// Dto 컨버터
	private PostDetailResponse toResponseDto(Post post) {
		return PostDetailResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.author(post.getIsAnonymous() ? "익명" : post.getMember().getName())
			.isAnonymous(post.getIsAnonymous())
			.viewCount(post.getViewCount())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.communityCategory(post.getCommunityCategory())
			.postedAt(post.getRegisteredAt())
			.lastUpdatedAt(post.getUpdatedAt())
			.build();
	}
}