package com.palbang.unsemawang.community.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(PostDetailController.class) // Controller 계층만 로드
class PostDetailControllerTest {

	@Autowired
	private MockMvc mockMvc; // MockMvc 객체를 통해 HTTP 요청 테스트

	@MockBean
	private PostDetailService postDetailService; // Service 계층 Mocking

	@Test
	@DisplayName("게시글 상세정보를 성공적으로 반환한다.")
	void testGetPostDetail_Success() throws Exception {
		// given
		Long postId = 1L;
		PostDetailResponse postDetailResponse = PostDetailResponse.builder()
			.id(postId)
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.author("테스트 작성자")
			.isAnonymous(false)
			.viewCount(123)
			.likeCount(10)
			.commentCount(5)
			.communityCategory(CommunityCategory.FREE_BOARD)
			.postedAt(LocalDateTime.of(2023, 12, 1, 10, 0))
			.lastUpdatedAt(LocalDateTime.of(2023, 12, 2, 10, 0))
			.build();

		when(postDetailService.getPostDetail(postId)).thenReturn(postDetailResponse);

		// when & then
		mockMvc.perform(get("/api/posts/{id}", postId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(postId.intValue())))
			.andExpect(jsonPath("$.title", is("테스트 게시글 제목")))
			.andExpect(jsonPath("$.content", is("테스트 게시글 내용")))
			.andExpect(jsonPath("$.author", is("테스트 작성자")))
			.andExpect(jsonPath("$.isAnonymous", is(false)))
			.andExpect(jsonPath("$.viewCount", is(123)))
			.andExpect(jsonPath("$.likeCount", is(10)))
			.andExpect(jsonPath("$.commentCount", is(5)))
			.andExpect(jsonPath("$.communityCategory", is("FREE_BOARD")))
			.andExpect(jsonPath("$.postedAt", is("2023-12-01T10:00:00")))
			.andExpect(jsonPath("$.lastUpdatedAt", is("2023-12-02T10:00:00")));
	}

	@Test
	@DisplayName("게시글이 없을 경우 404 Not Found를 반환한다.")
	void testGetPostDetail_NotFound() throws Exception {
		// given
		Long postId = 1L;
		when(postDetailService.getPostDetail(postId))
			.thenThrow(new EntityNotFoundException("게시글을 찾을 수 없습니다."));

		// when & then
		mockMvc.perform(get("/api/posts/{id}", postId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error", is("게시글을 찾을 수 없습니다.")))
			.andExpect(jsonPath("$.status", is(404)));
	}
}