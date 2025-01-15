package com.palbang.unsemawang.fortune.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequest;
import com.palbang.unsemawang.fortune.dto.response.FortuneInfoRegisterResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoRegisterService;

@WebMvcTest(
	controllers = FortuneInfoRegisterController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureDataJpa // @EnableJpaAuditing 때문에 JPA 관련 빈이 필요함
class FortuneInfoRegisterControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	private FortuneUserInfoRegisterService registerService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("사주 정보 등록_성공")
	void registerSuccess() throws Exception {
		// Given
		FortuneInfoRegisterRequest reqDto = createReq();
		FortuneInfoRegisterResponseDto respDto = createResp();

		when(registerService.registerFortuneInfo(any(FortuneInfoRegisterRequest.class))).thenReturn(respDto);

		// When
		mockMvc.perform(post("/fortune-users")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(objectMapper.writeValueAsString(reqDto))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(print());

		// Then
		verify(registerService, times(1)).registerFortuneInfo(any(FortuneInfoRegisterRequest.class));
	}

	private FortuneInfoRegisterRequest createReq() throws Exception {
		return FortuneInfoRegisterRequest.builder()
			.memberId("aaa")
			.relationName("가족")
			.name("홍길동")
			.sex('남')
			.year(2000)
			.month(8)
			.day(16)
			.hour(2)
			.solunar("solar")
			.youn(1)
			.build();
	}

	private FortuneInfoRegisterResponseDto createResp() throws Exception {
		return FortuneInfoRegisterResponseDto.builder()
			.memberId("aaa")
			.relationName("가족")
			.name("홍길동")
			.sex('남')
			.year(2020)
			.month(8)
			.day(16)
			.hour(2)
			.solunar("solar")
			.youn(1)
			.build();
	}
}