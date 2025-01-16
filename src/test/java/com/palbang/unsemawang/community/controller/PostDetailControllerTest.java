package com.palbang.unsemawang.community.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
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
@AutoConfigureDataJpa
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
			.userId("test-member-id")
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.nickname("테스트 닉네임") // nickname 설정
			.isAnonymous(false)
			.viewCount(123)
			.likeCount(10)
			.commentCount(5)
			.communityCategory(CommunityCategory.FREE_BOARD) // Enum 사용
			.registeredAt(java.time.LocalDateTime.of(2023, 12, 1, 10, 0))
			.updatedAt(java.time.LocalDateTime.of(2023, 12, 2, 10, 0))
			.build();

		when(postDetailService.getPostDetail(1L)).thenReturn(postDetailResponse);

		// when & then
		mockMvc.perform(get("/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.userId").value("test-member-id")) // userId 테스트 추가
			.andExpect(jsonPath("$.title").value("테스트 게시글 제목"))
			.andExpect(jsonPath("$.content").value("테스트 게시글 내용"))
			.andExpect(jsonPath("$.nickname").value("테스트 닉네임")) // nickname 확인
			.andExpect(jsonPath("$.isAnonymous").value(false))
			.andExpect(jsonPath("$.viewCount").value(123))
			.andExpect(jsonPath("$.likeCount").value(10))
			.andExpect(jsonPath("$.commentCount").value(5))
			.andExpect(jsonPath("$.communityCategory").value("FREE_BOARD")); // Enum 값 확인

		verify(postDetailService, times(1)).getPostDetail(1L);
	}

	@Test
	void testGetPostDetail_AnonymousPost() throws Exception {
		// given
		PostDetailResponse postDetailResponse = PostDetailResponse.builder()
			.id(1L)
			.userId("test-member-id")
			.title("테스트 게시글 제목")
			.content("테스트 게시글 내용")
			.nickname("익명") // 익명 처리
			.isAnonymous(true) // 익명 설정
			.viewCount(200)
			.likeCount(15)
			.commentCount(3)
			.communityCategory(CommunityCategory.FREE_BOARD)
			.registeredAt(java.time.LocalDateTime.of(2023, 12, 1, 10, 0))
			.updatedAt(java.time.LocalDateTime.of(2023, 12, 2, 15, 30))
			.build();

		when(postDetailService.getPostDetail(1L)).thenReturn(postDetailResponse);

		// when & then
		mockMvc.perform(get("/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.userId").value("test-member-id")) // userId 확인
			.andExpect(jsonPath("$.nickname").value("익명")) // 익명 닉네임 확인
			.andExpect(jsonPath("$.isAnonymous").value(true)) // 익명 여부 확인
			.andExpect(jsonPath("$.viewCount").value(200))
			.andExpect(jsonPath("$.likeCount").value(15))
			.andExpect(jsonPath("$.commentCount").value(3))
			.andExpect(jsonPath("$.communityCategory").value("FREE_BOARD"));

		verify(postDetailService, times(1)).getPostDetail(1L);
	}

	@Test
	void testGetPostDetail_NotFound() throws Exception {
		// given
		when(postDetailService.getPostDetail(1L))
			.thenThrow(new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		// when & then
		mockMvc.perform(get("/posts/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());

		verify(postDetailService, times(1)).getPostDetail(1L);
	}
}