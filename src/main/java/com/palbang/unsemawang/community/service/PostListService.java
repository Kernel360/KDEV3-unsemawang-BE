package com.palbang.unsemawang.community.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostListService {

	private final PostRepository postRepository;

	public LongCursorResponse<PostListResponse> getPostList(CommunityCategory category,
		CursorRequest<Long> cursorRequest) {
		List<Post> posts;

		// 요청 크기 기반 페이징 설정
		var pageable = PageRequest.of(0, cursorRequest.size());

		// 카테고리별 게시글 조회
		if (category == CommunityCategory.POPULAR_BOARD) {
			posts = postRepository.findPopularPosts(cursorRequest.key(), pageable);
		} else {
			posts = postRepository.findLatestPostsByCategory(category, cursorRequest.key(), pageable);
		}

		// 응답 데이터 매핑
		List<PostListResponse> data = posts.stream()
			.map(PostListResponse::fromEntity)
			.toList();

		// 다음 커서 생성
		Long nextCursor = !data.isEmpty() ? data.get(data.size() - 1).getCursorId() : null;

		// LongCursorResponse를 반환
		return LongCursorResponse.of(cursorRequest.next(nextCursor), data);
	}
}