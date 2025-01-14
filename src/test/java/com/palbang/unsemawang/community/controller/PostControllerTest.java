package com.palbang.unsemawang.community.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.service.PostService;

@WebMvcTest(
	controllers = PostController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureDataJpa // @EnableJpaAuditing 때문에 JPA 관련 빈이 필요함
class PostControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService; // 서비스 모킹

	@Autowired
	private ObjectMapper objectMapper; // JSON 직렬화에 사용

	@Test
	@DisplayName("등록 테스트 - 제목 글자 수 초과")
	public void post_failedValidation() throws Exception {
		// given
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.title("제목이란다라림쥐안녕하세요30자넘기기위해서제가이렇게노력하는모습을봐주세요로로라리리이히히히힣히히힣ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ")
			.category(CommunityCategory.FREE_BOARD)
			.isAnonymous(false)
			.content("hello")
			.build();

		// when, then : 요청을 보내면 Vaild 예외가 발생해야한다
		mockMvc.perform(post("/posts")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(postRegisterRequest)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("제목은 30자 이내여야 합니다"));
	}
}