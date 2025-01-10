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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListDto;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

@WebMvcTest(
	controllers = FortuneContentController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureDataJpa // @EnableJpaAuditing 때문에 JPA 관련 빈이 필요함
class FortuneContentControllerTest {

	@Autowired
	private MockMvc mockMvc; // 실제 테스트는 MockMvc를 통해 이루어짐

	@MockBean
	private FortuneContentService fortuneContentService; // 서비스 모킹

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
		when(fortuneContentService.getList()).thenReturn(ContentReadListDto.of(contentList));

		// 3. then - 성공 상태 코드와 함께 DataResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(fortuneContent1.getId()));
	}

	@Test
	@DisplayName("목록 조회 테스트 - 빈 리스트 조회 ")
	public void read() throws Exception {
		// 2. when - 서비스의 목록 조회 결과 예외 발생할 때
		when(fortuneContentService.getList()).thenReturn(new ArrayList<>());

		// 3. then - 성공 상태 코드와 함께 DataResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(0));
	}

	@Test
	@DisplayName("상세 조회 테스트 - GeneralException 발생")
	public void readDetail_notExistId() throws Exception {
		// 1. given - 발생할 예외
		Long notExistId = -999L;
		ResponseCode responseCode = ResponseCode.NOT_EXIST_UNIQUE_NO;
		GeneralException e = new GeneralException(responseCode);

		// 2. when - 서비스의 목록 조회 결과 예외가 발생할 때
		when(fortuneContentService.getContentById(notExistId)).thenThrow(e);

		// 3. then - 실패 상태 코드와 함께 ErrorResponse가 응답되어야 한다
		mockMvc.perform(get("/fortune-contents/{id}", notExistId)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.responseCode").value(responseCode.getCode()));
	}

	/* 헬퍼 */
	private FortuneContent createFortuneContent(int i) {
		return FortuneContent.builder()
			.nameEn("test-name" + i)
			.nameKo("테스트명" + i)
			.path("/test-path" + i)
			.isVisible(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}