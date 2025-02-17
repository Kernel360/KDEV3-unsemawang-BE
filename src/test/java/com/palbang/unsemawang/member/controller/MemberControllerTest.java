package com.palbang.unsemawang.member.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.WithCustomMockUser;
import com.palbang.unsemawang.chemistry.batch.TotalCalculationService;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoRegisterService;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoUpdateService;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;
import com.palbang.unsemawang.member.service.MemberService;

@WebMvcTest(controllers = MemberController.class)
@WithCustomMockUser
class MemberControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@MockBean
	private FortuneUserInfoRegisterService fortuneUserInfoRegisterService;

	@MockBean
	private FortuneUserInfoUpdateService fortuneUserInfoUpdateService;

	@MockBean
	private FileService fileService;

	@MockBean
	private TotalCalculationService totalCalculationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName(value = "추가정보 입력 - 오늘 날짜의 생년월일로 회원가입할 경우 성공")
	public void todayDate_signup() throws Exception {
		// given
		SignupExtraInfoRequest request = SignupExtraInfoRequest.builder()
			.nickname("testnick")
			.sex('여')
			.year(LocalDate.now().getYear())
			.month(LocalDate.now().getMonthValue())
			.day(LocalDate.now().getDayOfMonth())
			.hour(2)
			.solunar("lunar")
			.youn(1)
			.build();

		// when, then
		mockMvc.perform(post("/member/signup/extra-info")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "추가정보 입력 - 내일 날짜의 생년월일로 회원가입할 경우 실패")
	public void tomorrowDate_signup() throws Exception {
		// given
		SignupExtraInfoRequest request = SignupExtraInfoRequest.builder()
			.nickname("testnick")
			.sex('여')
			.year(LocalDate.now().plusDays(1).getYear())
			.month(LocalDate.now().plusDays(1).getMonthValue())
			.day(LocalDate.now().plusDays(1).getDayOfMonth())
			.hour(2)
			.solunar("lunar")
			.youn(1)
			.build();

		// when, then
		mockMvc.perform(post("/member/signup/extra-info")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("올바른 생년월일을 입력해주세요"));
	}
}