package com.palbang.unsemawang.fortune.controller;

import static com.palbang.unsemawang.common.constants.ResponseCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoReadResponseDto;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoReadService;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false) // Security 필터 비활성화
class FortuneUserInfoReadControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	private FortuneUserInfoReadService readService;

	@Test
	@DisplayName("사주정보 조회 성공 테스트")
	void testGetUserInfoList_success() throws Exception {
		// Given
		String memberId = "aaa";

		List<FortuneUserInfoReadResponseDto> responseList = createFortuneInfo();

		when(readService.fortuneInfoListRead(memberId)).thenReturn(responseList);

		mockMvc.perform(get("/fortune-users")
				.param("memberId", memberId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].relationName").value("가족"))
			.andExpect(jsonPath("$[0].name").value("홍길동"))
			.andExpect(jsonPath("$[0].sex").value("남"))
			.andExpect(jsonPath("$[0].year").value(2020))
			.andExpect(jsonPath("$[0].month").value(2))
			.andExpect(jsonPath("$[0].day").value(2))
			.andExpect(jsonPath("$[0].solunar").value("solar"))
			.andExpect(jsonPath("$[0].youn").value(0));

		verify(readService, times(1)).fortuneInfoListRead(memberId);
	}

	@Test
	@DisplayName("사주정보 조회 실패 테스트 - 회원 없음")
	void testGetUserInfoList_fail_memberNotFound() throws Exception {
		// Given
		String memberId = "invalid";

		when(readService.fortuneInfoListRead(memberId))
			.thenThrow(new GeneralException(NOT_EXIST_ID));

		// When & Then
		mockMvc.perform(get("/fortune-users")
				.param("memberId", memberId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest()) // HTTP 400
			.andExpect(jsonPath("$.message").value("유효하지 않은 요청 id"));

		verify(readService, times(1)).fortuneInfoListRead(memberId);
	}

	public List<FortuneUserInfoReadResponseDto> createFortuneInfo() {
		return List.of(
			FortuneUserInfoReadResponseDto.builder()
				.relationName("가족")
				.name("홍길동")
				.sex('남')
				.year(2020)
				.month(2)
				.day(2)
				.hour(0)
				.solunar("solar")
				.youn(0)
				.build(),
			FortuneUserInfoReadResponseDto.builder()
				.relationName("친구")
				.name("김철수")
				.sex('남')
				.year(1965)
				.month(6)
				.day(15)
				.hour(0)
				.solunar("lunar")
				.youn(1)
				.build()
		);
	}
}