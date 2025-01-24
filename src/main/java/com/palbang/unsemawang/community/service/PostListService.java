package com.palbang.unsemawang.community.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostListService {

	private final PostRepository postRepository;
	private final FileService fileService;

	public LongCursorResponse<PostListResponse> getPostList(
		CommunityCategory category,
		Sortingtype sort,
		CursorRequest<Long> cursorRequest) {

		// 요청 크기 기반 페이징 설정
		var pageable = PageRequest.of(0, cursorRequest.size());

		// 게시글 조회 분기 처리 (로직 메서드 분리)
		List<Post> posts = fetchPosts(category, sort, cursorRequest.key(), pageable);

		// 응답 데이터 매핑
		List<PostListResponse> data = posts.stream()
			.map(post -> PostListResponse.fromEntity(
				post,
				fileService.getPostThumbnailImgUrl(post.getId()),
				post.getIsAnonymous() ? null : fileService.getProfileImgUrl(post.getWriterId())))
			.toList();

		// 다음 커서 생성
		Long nextCursor = !data.isEmpty() ? data.get(data.size() - 1).getCursorId() : null;

		// LongCursorResponse 반환
		return LongCursorResponse.of(cursorRequest.next(nextCursor), data);
	}

	// Helper 메서드: 게시글 조회 분기 처리
	private List<Post> fetchPosts(CommunityCategory category, Sortingtype sort, Long cursorId, Pageable pageable) {
		if (Sortingtype.MOST_VIEWED.equals(sort)) {
			return postRepository.findMostViewedPostsByCategory(category, cursorId, pageable);
		}
		if (CommunityCategory.POPULAR_BOARD.equals(category)) {
			LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
			return postRepository.findPopularPosts(cursorId, thirtyDaysAgo, pageable);
		}
		if (Sortingtype.LATEST.equals(sort)) {
			return postRepository.findLatestPostsByCategory(category, cursorId, pageable);
		}
		throw new IllegalArgumentException("지원하지 않는 정렬 옵션입니다. 'latest' 또는 'mostViewed' 만 가능합니다.");
	}
}