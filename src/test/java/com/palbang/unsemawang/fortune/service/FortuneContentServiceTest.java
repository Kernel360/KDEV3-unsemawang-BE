package com.palbang.unsemawang.fortune.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.repository.FortuneContentRepository;

@SpringBootTest(classes = FortuneContentService.class)
class FortuneContentServiceTest {
	@Autowired
	private FortuneContentService fortuneContentService;

	@MockBean
	private FortuneContentRepository fortuneContentRepository;

	/**
	 *  [ 조회 테스트 ]
	 *  - 없는 식별 번호로 조회할 경우 GeneralException 발생
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
}