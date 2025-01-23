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
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청

		// Mock 반환 데이터 (Post 목록 생성)
		Post post1 = createPost(1L, "Title 1", true);
		Post post2 = createPost(2L, "Title 2", true);
		List<Post> mockPosts = List.of(post1, post2);

		// PostRepository의 메서드 호출 행동 정의(Mock behavior)
		when(postRepository.findLatestPostsByCategory(category, null, PageRequest.of(0, 2)))
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).hasSize(2);

		assertThat(response.data().get(0).getId()).isEqualTo(1L);
		assertThat(response.data().get(1).getId()).isEqualTo(2L);
		assertThat(response.nextCursorRequest().key()).isEqualTo(2L);
	}

	@Test
	void testGetPostList_emptyResult() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(null, 2); // 첫 페이지 요청

		// Mock 반환 데이터가 비어있는 경우
		List<Post> mockPosts = List.of();

		when(postRepository.findLatestPostsByCategory(category, null, PageRequest.of(0, 2)))
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).isEmpty(); // 데이터가 없는지 확인
		assertThat(response.nextCursorRequest().key()).isNull(); // 다음 커서가 없는지 확인
	}

	@Test
	void testGetPostList_nextPage() {
		// given
		CommunityCategory category = CommunityCategory.FREE_BOARD;
		CursorRequest<Long> cursorRequest = new CursorRequest<>(2L, 2); // 두 번째 페이지 요청

		// Mock 반환 데이터 (Post 목록 생성)
		Post post3 = createPost(3L, "Title 3", true);
		Post post4 = createPost(4L, "Title 4", true);
		List<Post> mockPosts = List.of(post3, post4);

		when(postRepository.findLatestPostsByCategory(category, 2L, PageRequest.of(0, 2)))
			.thenReturn(mockPosts);

		// when
		LongCursorResponse<PostListResponse> response = postListService.getPostList(category, cursorRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.data()).hasSize(2);

		assertThat(response.data().get(0).getId()).isEqualTo(3L);
		assertThat(response.data().get(1).getId()).isEqualTo(4L);
		assertThat(response.nextCursorRequest().key()).isEqualTo(4L);
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