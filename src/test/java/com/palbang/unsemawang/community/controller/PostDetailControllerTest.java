package com.palbang.unsemawang.community.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;

@WebMvcTest(
	controllers = PostDetailController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PostDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostDetailService postDetailService;

	@Test
	void testGetPostDetail_Success() throws Exception {
		// given
		PostDetailResponse postDetailResponse = PostDetailResponse.builder()
			.id(1L)
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.author("테스트 작성자")
			.isAnonymous(false)
			.viewCount(123)
			.likeCount(10)
			.commentCount(5)
			.communityCategory(CommunityCategory.FREE_BOARD) // Enum 사용
			.postedAt(java.time.LocalDateTime.of(2023, 12, 1, 10, 0))
			.lastUpdatedAt(java.time.LocalDateTime.of(2023, 12, 2, 10, 0))
			.build();

		when(postDetailService.getPostDetail(1L)).thenReturn(postDetailResponse);

		// when & then
		mockMvc.perform(get("/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.title").value("테스트 게시글 제목"))
			.andExpect(jsonPath("$.content").value("테스트 게시글 내용"))
			.andExpect(jsonPath("$.author").value("테스트 작성자"))
			.andExpect(jsonPath("$.isAnonymous").value(false))
			.andExpect(jsonPath("$.viewCount").value(123))
			.andExpect(jsonPath("$.likeCount").value(10))
			.andExpect(jsonPath("$.commentCount").value(5))
			.andExpect(jsonPath("$.communityCategory").value("FREE_BOARD")); // Enum 값 확인
		verify(postDetailService, times(1)).getPostDetail(1L);
	}

	@Test
	void testGetPostDetail_NotFound() throws Exception {
		// given
		when(postDetailService.getPostDetail(1L))
			.thenThrow(new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다."));

		// when & then
		mockMvc.perform(get("/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
		// .andExpect(jsonPath("$.success").value(false))
		// .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
		// .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));

		verify(postDetailService, times(1)).getPostDetail(1L);
	}
}