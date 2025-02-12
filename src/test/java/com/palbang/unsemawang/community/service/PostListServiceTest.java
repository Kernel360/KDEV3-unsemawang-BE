package com.palbang.unsemawang.community.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.CommunityListCategory;
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
		CommunityListCategory category = CommunityListCategory.FREE_BOARD;
		CommunityCategory communityCategory = CommunityCategory.valueOf(category.name());
		Sortingtype sort = Sortingtype.LATEST;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청 (size = 2)

		// Mock 반환 데이터 (size + 1 원칙에 따른 데이터 생성)
		Post post1 = createPost(1L, "Title 1");
		Post post2 = createPost(2L, "Title 2");
		Post post3 = createPost(3L, "Title 3"); // 초과 데이터
		List<Post> mockPosts = List.of(post1, post2, post3);

		when(postRepository.findLatestPostsByCategory(communityCategory, null, null, 3)) // size + 1로 호출
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, sort, cursorRequest);

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
		CommunityListCategory category = CommunityListCategory.FREE_BOARD;
		CommunityCategory communityCategory = CommunityCategory.valueOf(category.name());
		Sortingtype sort = Sortingtype.LATEST;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청 (size = 2)

		// Mock 반환 데이터가 비어있는 경우
		List<Post> mockPosts = List.of();

		when(postRepository.findLatestPostsByCategory(communityCategory, null, null, 3)) // size + 1로 호출
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, sort, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).isEmpty(); // 데이터가 없는지 확인
		assertThat(response.hasNextCursor()).isFalse(); // hasNextCursor가 false로 설정되었는지 확인
		assertThat(response.nextCursorRequest().key()).isNull(); // 다음 커서가 없는지 확인
	}

	@Test
	void testGetPostList_nextPage() {
		// given
		CommunityListCategory category = CommunityListCategory.FREE_BOARD;
		CommunityCategory communityCategory = CommunityCategory.valueOf(category.name());
		Sortingtype sort = Sortingtype.LATEST;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(2L, 2); // 두 번째 페이지 요청 (size = 2)
		LocalDateTime cursorRegisteredAt = LocalDateTime.now().minusDays(1); // Mock cursorKey에 해당하는 registeredAt 값

		// Mock 반환 데이터 (size + 1 원칙에 따른 데이터 생성)
		Post post3 = createPost(3L, "Title 3");
		Post post4 = createPost(4L, "Title 4");
		Post post5 = createPost(5L, "Title 5"); // 초과 데이터
		List<Post> mockPosts = List.of(post3, post4, post5);

		when(postRepository.findRegisteredAtById(2L)) // cursorKey = 2L의 registeredAt 조회
			.thenReturn(cursorRegisteredAt);
		when(postRepository.findLatestPostsByCategory(communityCategory, 2L, cursorRegisteredAt,
			3)) // cursorKey와 cursorRegisteredAt 전달
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, sort, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).hasSize(2); // 요청한 size만 적용
		assertThat(response.hasNextCursor()).isTrue(); // 초과 데이터 존재
		assertThat(response.nextCursorRequest().key()).isEqualTo(4L); // 마지막 반환 데이터의 ID 활용
	}

	@Test
	void testGetPostList_sortedByViewCount() {
		// given
		CommunityListCategory category = CommunityListCategory.FREE_BOARD;
		CommunityCategory communityCategory = CommunityCategory.valueOf(category.name());
		Sortingtype sort = Sortingtype.MOST_VIEWED;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 3); // 첫 페이지 요청 (size = 3)

		// Mock 반환 데이터 (조회수 순서로 정렬됨, size + 1 원칙에 따라 데이터 생성)
		Post post1 = createPostWithViews(1L, "Title 1", 50); // 조회수 50
		Post post2 = createPostWithViews(2L, "Title 2", 20); // 조회수 20
		Post post3 = createPostWithViews(3L, "Title 3", 10); // 조회수 10
		Post post4 = createPostWithViews(4L, "Title 4", 5);  // 초과 데이터
		List<Post> mockPosts = List.of(post1, post2, post3, post4);

		when(postRepository.findMostViewedPostsByCategory(communityCategory, null, 4))
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, sort, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).hasSize(3); // 최대 size만큼 반환
		assertThat(response.hasNextCursor()).isTrue();          // 초과 데이터가 있으므로 hasNextCursor는 true
		assertThat(response.nextCursorRequest().key()).isEqualTo(3L);
	}

	// 조회수를 포함한 Post 객체 생성 헬퍼 메서드
	private Post createPostWithViews(Long id, String title, int viewCount) {
		return Post.builder()
			.id(id)
			.title(title)
			.isVisible(true)
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

	private Post createPost(Long id, String title) {
		return Post.builder()
			.id(id)
			.title(title)
			.isVisible(true)
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