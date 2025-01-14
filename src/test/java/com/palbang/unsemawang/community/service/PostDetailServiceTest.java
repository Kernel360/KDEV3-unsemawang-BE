package com.palbang.unsemawang.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.EntityNotFoundException;

class PostDetailServiceTest {

	@InjectMocks
	private PostDetailService postDetailService;

	@Mock
	private PostRepository postRepository;

	private Post post;  // 테스트용 가상 Post 데이터

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock 데이터 설정
		Member member = Member.builder()
			.id("test-member-id")
			.name("테스트 작성자")
			.build();

		post = Post.builder()
			.id(1L)
			.member(member)
			.isAnonymous(false)
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.viewCount(123)
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
		assertEquals("테스트 게시글 제목", response.getTitle());
		assertEquals("테스트 게시글 내용", response.getContent());
		assertEquals("테스트 작성자", response.getAuthor());
		assertFalse(response.getIsAnonymous());
		assertEquals(123, response.getViewCount());
		assertEquals(10, response.getLikeCount());
		assertEquals(5, response.getCommentCount());
		assertEquals(CommunityCategory.FREE_BOARD, response.getCommunityCategory());
		assertEquals(java.time.LocalDateTime.of(2023, 12, 1, 10, 0), response.getPostedAt());
		assertEquals(java.time.LocalDateTime.of(2023, 12, 2, 10, 0), response.getLastUpdatedAt());

		// verify
		verify(postRepository, times(1)).findById(1L);
	}

	@Test
	void testGetPostDetail_NotFound() {
		// given
		when(postRepository.findById(1L)).thenReturn(Optional.empty());

		// when
		Exception exception = assertThrows(EntityNotFoundException.class, () ->
			postDetailService.getPostDetail(1L));

		// then
		assertEquals("게시글을 찾을 수 없습니다.", exception.getMessage());

		// verify
		verify(postRepository, times(1)).findById(1L);
	}
}