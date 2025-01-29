package com.palbang.unsemawang.community.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;

public class PostListServiceTest {

	@InjectMocks
	private PostListService postListService; // 테스트 대상 클래스

	@Mock
	private FileService fileService;
	
	@Mock
	private PostRepository postRepository; // Mock 객체로 설정

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // @Mock 주석으로 선언된 모든 Mock 객체 초기화
	}

	@Test
	void testGetPostList_firstPage() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청 (size = 2)

		// Mock 반환 데이터 (size + 1 원칙에 따른 데이터 생성)
		Post post1 = createPost(1L, "Title 1", true);
		Post post2 = createPost(2L, "Title 2", true);
		Post post3 = createPost(3L, "Title 3", true); // 초과 데이터
		List<Post> mockPosts = List.of(post1, post2, post3);

		when(postRepository.findLatestPostsByCategory(category, null, 3)) // size + 1로 호출
				.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, Sortingtype.LATEST,
				cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).hasSize(2);
		assertThat(response.hasNextCursor()).isTrue(); // 초과 데이터 존재하므로 true
		assertThat(response.data().get(0).getId()).isEqualTo(1L);
		assertThat(response.data().get(1).getId()).isEqualTo(2L);
		assertThat(response.nextCursorRequest().key()).isEqualTo(2L); // 마지막 데이터의 ID
	}

	@Test
	void testGetPostList_emptyResult() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청 (size = 2)

		// Mock 반환 데이터가 비어있는 경우
		List<Post> mockPosts = List.of();

		when(postRepository.findLatestPostsByCategory(category, null, 3)) // size + 1로 호출
				.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, Sortingtype.LATEST,
				cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).isEmpty(); // 데이터가 없는지 확인
		assertThat(response.hasNextCursor()).isFalse(); // hasNextCursor가 false로 설정되었는지 확인
		assertThat(response.nextCursorRequest().key()).isNull(); // 다음 커서가 없는지 확인
	}

	@Test
	void testGetPostList_nextPage() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(2L, 2); // 두 번째 페이지 요청 (size = 2)

		// Mock 반환 데이터 (size + 1 원칙에 따른 데이터 생성)
		Post post3 = createPost(3L, "Title 3", true);
		Post post4 = createPost(4L, "Title 4", true);
		Post post5 = createPost(5L, "Title 5", true); // 초과 데이터
		List<Post> mockPosts = List.of(post3, post4, post5);

		when(postRepository.findLatestPostsByCategory(category, 2L, 3)) // size + 1로 호출
				.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, Sortingtype.LATEST,
				cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.hasNextCursor()).isTrue(); // 초과 데이터가 있으므로 true
		assertThat(response.data()).hasSize(2); // 요청 size대로만 반환
		assertThat(response.data().get(0).getId()).isEqualTo(3L);
		assertThat(response.data().get(1).getId()).isEqualTo(4L);
		assertThat(response.nextCursorRequest().key()).isEqualTo(4L); // 마지막 데이터의 ID
	}

	@Test
	void testGetPostList_sortedByViewCount() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 3); // 첫 페이지 요청 (size = 3)

		// Mock 반환 데이터 (조회수 순서로 정렬됨, size + 1 원칙에 따라 데이터 생성)
		Post post1 = createPostWithViews(1L, "Title 1", true, 50); // 조회수 50
		Post post2 = createPostWithViews(2L, "Title 2", true, 20); // 조회수 20
		Post post3 = createPostWithViews(3L, "Title 3", true, 10); // 조회수 10
		Post post4 = createPostWithViews(4L, "Title 4", true, 5);  // 초과 데이터
		List<Post> mockPosts = List.of(post1, post2, post3, post4);

		when(postRepository.findMostViewedPostsByCategory(category, null, 4)) // size + 1로 호출
				.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, Sortingtype.MOST_VIEWED,
				cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.hasNextCursor()).isTrue(); // 초과 데이터 존재
		assertThat(response.data()).hasSize(3); // 최대 size만큼 반환
		assertThat(response.data().get(0).getId()).isEqualTo(1L); // ID 1, 조회수 50
		assertThat(response.data().get(1).getId()).isEqualTo(2L); // ID 2, 조회수 20
		assertThat(response.data().get(2).getId()).isEqualTo(3L); // ID 3, 조회수 10
		assertThat(response.nextCursorRequest().key()).isEqualTo(3L); // 마지막 반환 데이터의 ID
	}

	// 조회수를 포함한 Post 객체 생성 헬퍼 메서드
	private Post createPostWithViews(Long id, String title, boolean isVisible, int viewCount) {
		return Post.builder()
			.id(id)
			.title(title)
			.isVisible(isVisible)
			.content("Content for " + title)
			.viewCount(viewCount) // 조회수 설정
			.likeCount(0)
			.commentCount(0)
			.member(Member.builder()
				.nickname("Nickname " + id)
				.build())
			.communityCategory(CommunityCategory.FREE_BOARD)
			.build();
	}

	private Post createPost(Long id, String title, boolean isVisible) {
		return Post.builder()
			.id(id)
			.title(title)
			.isVisible(isVisible)
			.content("Content for " + title)
			.viewCount(0)
			.likeCount(0)
			.commentCount(0)
			.member(Member.builder()
				.nickname("Nickname " + id)
				.build())
			.communityCategory(CommunityCategory.FREE_BOARD)
			.build();
	}
}