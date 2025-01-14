package com.palbang.unsemawang.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.response.PostRegisterResponse;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;

@SpringBootTest(classes = PostService.class)
class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private PostRepository postRepository;

	@Test
	@DisplayName(value = "게시글 등록 - 모든 값이 정상적으로 들어온 경우")
	public void postRegisterTest() {
		// given
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.title("test-title")
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.isAnonymous(false)
			.build();

		Post post = Post.builder()
			.id(1L)
			.communityCategory(postRegisterRequest.getCategory())
			.title(postRegisterRequest.getTitle())
			.content(postRegisterRequest.getContent())
			.isAnonymous(postRegisterRequest.getIsAnonymous())
			.build();

		// when
		when(postRepository.save(any(Post.class))).thenReturn(post);

		// then
		PostRegisterResponse postRegisterResponse = postService.register(postRegisterRequest);
		assertNotNull(postRegisterResponse);
		assertEquals("게시글 등록이 성공했습니다!", postRegisterResponse.getMessage());
		verify(postRepository, times(1)).save(any());
	}

	@Test
	@DisplayName(value = "게시글 등록 - 저장이 안됐을 경우")
	public void postRegisterTest_saveFailure() {
		// given
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.isAnonymous(false)
			.build();

		Post post = Post.builder()
			.communityCategory(postRegisterRequest.getCategory())
			.title(postRegisterRequest.getTitle())
			.content(postRegisterRequest.getContent())
			.isAnonymous(postRegisterRequest.getIsAnonymous())
			.build();

		// when
		when(postRepository.save(any(Post.class))).thenReturn(post);

		// then
		assertThrows(GeneralException.class, () -> postService.register(postRegisterRequest));

		verify(postRepository, times(1)).save(any());
	}

}