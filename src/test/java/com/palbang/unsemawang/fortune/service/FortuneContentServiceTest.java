package com.palbang.unsemawang.fortune.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListDto;
import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.repository.FortuneContentRepository;

@SpringBootTest(classes = FortuneContentService.class)
class FortuneContentServiceTest {
	@Autowired
	private FortuneContentService fortuneContentService;

	@MockBean
	private FortuneContentRepository fortuneContentRepository;

	@MockBean
	private FileService fileService;

	/**
	 *  [ 조회 테스트 ]
	 *  - 없는 식별 번호로 조회할 경우 GeneralException 발생
	 *  - 카테고리명 없이 목록 조회를 할 경우 전체 목록 조회
	 *
	 */

	@Test
	@DisplayName("조회 테스트 | 실패 - 없는 식별 번호로 조회할 경우 예외 발생")
	public void getContent_notExistId() {
		// 1. given -  없는 id 세팅
		Long notExistId = -999L;

		when(fortuneContentRepository.findById(notExistId)).thenReturn(Optional.empty());

		// 2. when, then - 없는 id 값으로 컨텐츠 조회 시 예외 발생
		assertThrows(GeneralException.class,
			() -> fortuneContentService.getContentById(notExistId)
		);
	}

	@Test
	@DisplayName("조회 테스트 - 카테고리명 없이 목록 조회를 할 경우 전체 목록 조회")
	public void getContentList_noCategory() {
		// given
		FortuneCategory category1 = initFortuneCategoryEntity(100);
		FortuneCategory category2 = initFortuneCategoryEntity(100);
		List<FortuneContent> fortuneContentListOfC1 = List.of(
			initFortuneContentEntity(100, category1),
			initFortuneContentEntity(1000, category1)
		);
		List<FortuneContent> fortuneContentListOfC2 = List.of(
			initFortuneContentEntity(1000, category2)
		);
		List<FortuneContent> fortuneContentListAll = new ArrayList<>();
		fortuneContentListAll.addAll(fortuneContentListOfC1);
		fortuneContentListAll.addAll(fortuneContentListOfC2);

		// when
		when(fortuneContentRepository.findAll()).thenReturn(fortuneContentListAll);
		when(fortuneContentRepository.findAllByFortuneCategory(category1.getNameEn())).thenReturn(
			fortuneContentListOfC1);
		when(fortuneContentRepository.findAllByFortuneCategory(category2.getNameEn())).thenReturn(
			fortuneContentListOfC2);

		// then
		List<ContentReadListDto> getList = fortuneContentService.getList(null);
		assertEquals(fortuneContentListAll.size(), getList.size());

		verify(fortuneContentRepository, times(1)).findAll();
		verify(fortuneContentRepository, times(0)).findAllByFortuneCategory(anyString());
	}

	/* 헬퍼 */
	private FortuneContent initFortuneContentEntity(int i, FortuneCategory category) {
		return FortuneContent.builder()
			.id((long)i)
			.nameEn("test-name" + i)
			.nameKo("테스트명" + i)
			.fortuneCategory(category)
			.path("/test-path" + i)
			.isVisible(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	private FortuneCategory initFortuneCategoryEntity(int i) {
		return FortuneCategory.builder()
			.id((long)i)
			.description("short-desc" + i)
			.nameEn("test-name-en" + i)
			.name("test-name-kr" + i)
			.isVisible(true)
			.build();
	}
}