package com.palbang.unsemawang.community.service;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostDetailService {

	private final PostRepository postRepository;

	// getPostDetail 메서드 구현
	public PostDetailResponse getPostDetail(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다."));

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
