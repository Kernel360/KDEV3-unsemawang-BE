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

		// 게시글 조회
		List<Post> posts = fetchPosts(category, sort, cursorRequest.key(), cursorRequest.size());

		// hasNext 판단 후 초과 데이터 삭제
		boolean hasNext = posts.size() > cursorRequest.size();
		if (hasNext) {
			posts.remove(posts.size() - 1); // 초과 데이터 삭제
		}

		// 다음 커서 결정
		Long nextCursor = !posts.isEmpty() ? posts.get(posts.size() - 1).getId() : null;

		// 데이터 변환
		List<PostListResponse> data = posts.stream()
				.map(post -> PostListResponse.fromEntity(
						post,
						fileService.getPostThumbnailImgUrl(post.getId()),
						post.getIsAnonymous() ? null : fileService.getProfileImgUrl(post.getWriterId())))
				.toList();

		// LongCursorResponse 생성 및 반환
		return LongCursorResponse.of(cursorRequest.next(nextCursor), data);
	}

	// Helper 메서드: 게시글 조회 분기 처리
	private List<Post> fetchPosts(CommunityCategory category, Sortingtype sort, Long cursorId, int size) {
		if (Sortingtype.MOST_VIEWED.equals(sort)) {
			return postRepository.findMostViewedPostsByCategory(category, cursorId, size + 1); // size + 1 사용
		}
		if (CommunityCategory.POPULAR_BOARD.equals(category)) {
			LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
			return postRepository.findPopularPosts(cursorId, thirtyDaysAgo, size + 1); // size + 1 사용
		}
		if (Sortingtype.LATEST.equals(sort)) {
			return postRepository.findLatestPostsByCategory(category, cursorId, size + 1); // size + 1 사용
		}
		throw new IllegalArgumentException("지원하지 않는 정렬 옵션입니다. 'latest' 또는 'mostViewed' 만 가능합니다.");
	}
}