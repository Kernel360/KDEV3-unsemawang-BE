package com.palbang.unsemawang.community.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.WithCustomMockUser;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.service.PostService;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.entity.Member;

@WebMvcTest(
	controllers = PostController.class
)
class PostControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService; // 서비스 모킹

	@Autowired
	private ObjectMapper objectMapper; // JSON 직렬화에 사용

	@Test
	@WithCustomMockUser
	@DisplayName("등록 테스트 - 제목 글자 수 초과")
	public void post_failedValidation_titleSize() throws Exception {
		// given
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.title("제목이란다라림쥐안녕하세요30자넘기기위해서제가이렇게노력하는모습을봐주세요로로라리리이히히히힣히히힣ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ")
			.category(CommunityCategory.FREE_BOARD)
			.content("hello")
			.build();

		// when, then : 요청을 보내면 Vaild 예외가 발생해야한다
		mockMvc.perform(post("/posts")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(postRegisterRequest))
				.with(csrf())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("제목은 30자 이내여야 합니다"));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시글 수정 - 회원이 작성한 게시글이 아닐 경우 실패")
	public void postModifyTest_notPostOfMember() throws Exception {
		// given - 회원, 게시글 수정 요청 객체 생성
		Long postId = 1000L;
		PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
			.title("제목이란다라림쥐안녕")
			.category(CommunityCategory.FREE_BOARD)
			.content("hello")
			.build();

		when(postService.update(eq("testuser"), eq(postId), any(PostUpdateRequest.class))).thenThrow(
			new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "유효하지 않는 게시글 입니다")
		);

		// when, then : 요청을 보내면 유효하지 않는 게시글이라는 메세지가 떠야한다
		mockMvc.perform(put("/posts/{id}", postId)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(postUpdateRequest))
				.with(csrf())
			)
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("유효하지 않은 고유번호"));

		verify(postService, times(1)).update(eq("testuser"), eq(postId), any(PostUpdateRequest.class));
	}

	/* 헬퍼 */
	private Member createMember() {
		return Member.builder()
			.id("test-user")
			.role(MemberRole.GENERAL)
			.email("test@unsemawang.com")
			.isJoin(true)
			.build();
	}
}