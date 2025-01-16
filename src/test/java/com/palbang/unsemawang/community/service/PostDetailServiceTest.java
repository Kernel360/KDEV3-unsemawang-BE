package com.palbang.unsemawang.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;

class PostDetailServiceTest {

	@InjectMocks
	private PostDetailService postDetailService;

	@Mock
	private PostRepository postRepository;

	public PostDetailServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("게시글 조회 성공 (조회수 증가 검증)")
	void getPostDetail_SuccessfullyIncrementsViewCount() {
		// Given
		Long postId = 1L;
		String memberId = "test-member-id";

		Member member = Member.builder().id(memberId).build();
		Post post = Post.builder()
			.id(postId)
			.title("Test Post")
			.content("Content")
			.viewCount(10)
			.isVisible(false)
			.member(member)
			.build();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// Act
		PostDetailResponse response = postDetailService.getPostDetail(memberId, postId);

		// Assert
		assertNotNull(response);
		verify(postRepository).incrementViewCount(postId); // 조회수 증가 메서드 호출 확인
		assertEquals(postId, response.getId());
	}

	@Test
	@DisplayName("비공개 게시글 조회 성공 (작성자가 접근)")
	void getPostDetail_SuccessForPrivatePostByOwner() {
		// Given
		Long postId = 2L;
		String memberId = "test-member-id";

		Member member = Member.builder().id(memberId).build();
		Post post = Post.builder()
			.id(postId)
			.title("Private Post")
			.content("Private Content")
			.viewCount(15)
			.isVisible(true) // 비공개 게시글
			.member(member)
			.build();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// Act
		PostDetailResponse response = postDetailService.getPostDetail(memberId, postId);

		// Assert
		assertNotNull(response);
		verify(postRepository).incrementViewCount(postId); // 조회수 증가 메서드 호출 확인
		assertEquals(postId, response.getId());
	}

	@Test
	@DisplayName("비공개 게시글에 접근 시 작성자가 아니면 예외 발생")
	void getPostDetail_ThrowsExceptionForUnauthorizedAccessToPrivatePost() {
		// Given
		Long postId = 3L;
		String memberId = "unauthorized-user-id";

		Member owner = Member.builder().id("owner-id").build();
		Post post = Post.builder()
			.id(postId)
			.title("Private Post")
			.content("Unauthorized Access")
			.viewCount(20)
			.isVisible(true) // 비공개 게시글
			.member(owner)
			.build();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// Act & Assert
		GeneralException exception = assertThrows(
			GeneralException.class,
			() -> postDetailService.getPostDetail(memberId, postId)
		);

		assertEquals(ResponseCode.FORBIDDEN, exception.getErrorCode()); // 예외 코드 확인
		verify(postRepository, never()).incrementViewCount(postId); // 조회수는 증가하지 않음
	}

	@Test
	@DisplayName("존재하지 않는 게시글 요청 시 예외 발생")
	void getPostDetail_ThrowsExceptionForNonExistentPost() {
		// Given
		Long postId = 4L;
		String memberId = "test-member-id";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		// Act & Assert
		GeneralException exception = assertThrows(
			GeneralException.class,
			() -> postDetailService.getPostDetail(memberId, postId)
		);

		assertEquals(ResponseCode.RESOURCE_NOT_FOUND, exception.getErrorCode()); // 예외 코드 확인
		verify(postRepository, never()).incrementViewCount(postId); // 조회수는 증가하지 않음
	}
}