package com.palbang.unsemawang.fortune.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

@SpringBootTest
@AutoConfigureMockMvc
class FortuneContentControllerTest {

	@Autowired
	private MockMvc mockMvc; // 실제 테스트는 MockMvc를 통해 이루어짐

	@MockBean
	private FortuneContentService fortuneContentService;

	@Autowired
	private ObjectMapper objectMapper; // JSON 직렬화에 사용

	/**
	 *  [ 목록 조회 테스트 ]
	 *  1. 여러 개 데이터 조회 - 응답 상태 200, 성공 여부 true, 응답 데이터 ContentReadListDto 반환
	 *  2. 빈 리스트 조회 - 응답 상태 200, 성공 여부 true, 응답 데이터 빈 ContentReadListDto 반환
	 *
	 *  [ 상세 조회 테스트 ]
	 *  1.  GeneralException 발생 - 응답 상태 400, 성공 여부 false
	 */

	@Test
	@DisplayName("목록 조회 테스트 - 여러 개 데이터 조회 ")
	public void readList() throws Exception {
		// 1. given - 컨텐츠 목록 객체 생성
		FortuneContent fortuneContent1 = createFortuneContent(0);
		FortuneContent fortuneContent2 = createFortuneContent(100);
		FortuneContent fortuneContent3 = createFortuneContent(300);
		List<FortuneContent> contentList = List.of(fortuneContent1, fortuneContent2, fortuneContent3);

		// 2. when - 서비스의 목록 조회 메서드가 리스트를 반환할 때
		when(fortuneContentService.getList()).thenReturn(contentList);

		// 3. then - 성공 상태 코드와 함께 DataResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.contentList[0].id").value(fortuneContent1.getId()));
	}

	@Test
	@DisplayName("상세 조회 테스트 - GeneralException 발생")
	public void readDetail_notExistId() throws Exception {
		// 2. when - 서비스의 목록 조회 결과 예외 발생할 때
		when(fortuneContentService.getList()).thenReturn(new ArrayList<>());

		// 3. then - 성공 상태 코드와 함께 DataResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.contentList.size()").value(0));
	}

	@Test
	@DisplayName("목록 조회 테스트 - 빈 리스트 조회 ")
	public void read() throws Exception {
		// 1. given - 발생할 예외
		GeneralException e = new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "id: x");

		// 2. when - 서비스의 목록 조회 결과 예외가 발생할 때
		when(fortuneContentService.getList()).thenThrow(e);

		// 3. then - 실패 상태 코드와 함께 ErrorResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.message").value("유효하지 않은 고유번호 - id: x"));
	}

	/* 헬퍼 */
	private FortuneContent createFortuneContent(int i) {
		return FortuneContent.builder()
			.id((long)i)
			.fortuneContentName("test-name" + i)
			.description("test-desc" + i)
			.path("/test-path" + i)
			.isActive(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}