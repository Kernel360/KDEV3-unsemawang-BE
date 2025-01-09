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
import com.palbang.unsemawang.fortune.dto.request.SearchRequest;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListResponse;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

@WebMvcTest(
	controllers = FortuneContentController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureDataJpa // @EnableJpaAuditing 때문에 JPA 관련 빈이 필요함
class FortuneContentControllerSearchTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FortuneContentService fortuneContentService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 *  [ 검색 컨트롤러 테스트 ]
	 *  - 키워드가 전달된 경우 성공 응답
	 *  - 키워드가 전달되지 않은 경우 성공 응답
	 */
	@Test
	@DisplayName("검색 컨트롤러 테스트 - 키워드가 전달된 경우 성공 응답")
	void search_keyword() throws Exception {
		// 1. given - 키워드, 서비스 반환 리스트 세팅
		String keyword = "search-keyword";
		FortuneContent fortuneContent1 = createFortuneContent(1);
		FortuneContent fortuneContent2 = createFortuneContent(1000);
		ContentReadListResponse responseDto = ContentReadListResponse.of(List.of(fortuneContent1, fortuneContent2));
		SearchRequest searchRequest = new SearchRequest(keyword);

		// 2. when - 서비스 동작 등록
		when(fortuneContentService.getSearchList(searchRequest)).thenReturn(responseDto);

		// 3. then - 검색 요청 성공 응답 나와야함
		mockMvc.perform(get("/fortune-contents/search")
				.param("keyword", searchRequest.keyword())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.contentList.size()").value(responseDto.getContentList().size()))
			.andExpect(jsonPath("$.data.contentList[0].nameEn").value(responseDto.getContentList().get(0).getNameEn()));

	}

	@Test
	@DisplayName("검색 컨트롤러 테스트 - 키워드가 전달되지 않은 경우 성공 응답")
	void search_notKeyword() throws Exception {
		// 1. given - 키워드, 서비스 반환 리스트 세팅
		ContentReadListResponse responseDto = ContentReadListResponse.of(new ArrayList<>());

		// 2. when - 서비스 동작 등록
		when(fortuneContentService.getSearchList(new SearchRequest(null))).thenReturn(responseDto);

		// 3. then - 검색 요청 성공 응답 나와야함
		mockMvc.perform(get("/fortune-contents/search")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.contentList.size()").value(0));
	}

	/* 헬퍼 */
	FortuneContent createFortuneContent(int i) {
		return FortuneContent.builder()
			.id((long)i)
			.nameEn("test-name" + i)
			.nameKo("테스트명" + i)
			.path("/test-path" + i)
			.shortDescription("sort-desc" + i)
			.longDescription("long-desc" + i)
			.isVisible(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}