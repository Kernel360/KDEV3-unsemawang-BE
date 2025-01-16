package com.palbang.unsemawang.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;

class PostDetailServiceTest {

	@InjectMocks
	private PostDetailService postDetailService;

	@Mock
	private PostRepository postRepository;

	private Post post; // 테스트용 가상 Post 데이터

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock 데이터 설정
		Member member = Member.builder()
			.id("test-member-id")
			.nickname("테스트 닉네임")
			.name("테스트 이름")
			.build();

		post = Post.builder()
			.id(1L)
			.member(member)
			.isAnonymous(false)
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.viewCount(123) // 증가하기 전 조회수
			.likeCount(10)
			.commentCount(5)
			.communityCategory(CommunityCategory.FREE_BOARD)
			.registeredAt(java.time.LocalDateTime.of(2023, 12, 1, 10, 0))
			.updatedAt(java.time.LocalDateTime.of(2023, 12, 2, 10, 0))
			.build();
	}

	@Test
	void testGetPostDetail_Success() {
		// given
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));

		// when
		PostDetailResponse response = postDetailService.getPostDetail(1L);

		// then
		assertNotNull(response);
		assertEquals(1L, response.getId());
		assertEquals("test-member-id", response.getUserId());  // userId 검사
		assertEquals("테스트 게시글 제목", response.getTitle());
		assertEquals("테스트 게시글 내용", response.getContent());
		assertEquals("테스트 닉네임", response.getNickname());  // nickname 검사
		assertFalse(response.getIsAnonymous());
		assertEquals(124, response.getViewCount()); // 기존 조회수에 대한 증가 값 포함
		assertEquals(10, response.getLikeCount());
		assertEquals(5, response.getCommentCount());
		assertEquals(CommunityCategory.FREE_BOARD, response.getCommunityCategory());
		assertEquals(java.time.LocalDateTime.of(2023, 12, 1, 10, 0), response.getRegisteredAt());
		assertEquals(java.time.LocalDateTime.of(2023, 12, 2, 10, 0), response.getUpdatedAt());

		verify(postRepository, times(1)).findById(1L);
		verify(postRepository, times(1)).save(post); // handleViewCount가 호출되었는지 확인
	}

	@Test
	void testGetPostDetail_ViewCountIncreased() {
		// given
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));

		// when
		postDetailService.getPostDetail(1L);

		// then
		// 조회수가 증가했는지 확인
		assertEquals(124, post.getViewCount());

		// verify - 조회수 증가를 저장했는지 확인
		verify(postRepository, times(1)).save(post);
	}

	@Test
	void testGetPostDetail_NotFound() {
		// given
		when(postRepository.findById(1L)).thenReturn(Optional.empty());

		// when
		Exception exception = assertThrows(GeneralException.class, () -> postDetailService.getPostDetail(1L));

		// then
		assertEquals("요청한 리소스를 찾을 수 없습니다.", exception.getMessage());

		// verify
		verify(postRepository, times(1)).findById(1L);
		verify(postRepository, times(0)).save(any(Post.class)); // save는 호출되지 않아야 함
	}

	@Test
	void testGetPostDetail_AnonymousPost() {
		// given
		post = post.toBuilder()
			.isAnonymous(true) // 익명 설정
			.build();
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));

		// when
		PostDetailResponse response = postDetailService.getPostDetail(1L);

		// then
		assertNotNull(response);
		assertEquals("익명", response.getNickname()); // 익명인 경우 닉네임은 '익명'
		assertTrue(response.getIsAnonymous());
	}
}