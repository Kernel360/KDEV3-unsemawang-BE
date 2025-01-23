package com.palbang.unsemawang.fortune.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.palbang.unsemawang.config.QueryDslConfig;
import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.entity.FortuneContent;

@DataJpaTest
@Import(QueryDslConfig.class)
public class FortuneContentReadRepositoryTest {

	@Autowired
	private FortuneContentRepository fortuneContentRepository;
	@Autowired
	private FortuneCategoryRepository fortuneCategoryRepository;

	@Test
	@DisplayName("카테고리별 컨텐츠 조회 테스트 - 성공")
	public void readByCategory_success() {
		// given - 카테고리, 컨텐츠 리스트 등록
		FortuneCategory fc1 = initFortuneCategoryEntity(100);
		fc1 = fortuneCategoryRepository.save(fc1);
		FortuneCategory fc2 = initFortuneCategoryEntity(1000);
		fc2 = fortuneCategoryRepository.save(fc2);

		List<FortuneContent> fortuneContentList = List.of(
			initFortuneContentEntity(1, fc1),
			initFortuneContentEntity(100, fc1),
			initFortuneContentEntity(1000, fc2)
		);
		List<FortuneContent> savedList = fortuneContentRepository.saveAll(fortuneContentList);
		assertNotNull(savedList.get(0).getId());

		// when,then - 첫번째 카테고리로 조회할 경우 2개의 데이터가 담긴 리스트 번환
		List<FortuneContent> getList = fortuneContentRepository.findAllByFortuneCategory(fc1.getNameEn());
		assertEquals(2, getList.size());
		assertEquals(getList.get(0).getNameEn(), fortuneContentList.get(0).getNameEn());
		assertEquals(getList.get(1).getFortuneCategory(), fc1);

	}

	/* 헬퍼 */
	private FortuneContent initFortuneContentEntity(int i, FortuneCategory category) {
		return FortuneContent.builder()
			.nameEn("test-name" + i)
			.nameKo("테스트명" + i)
			.fortuneCategory(category)
			.path("/test-path" + i)
			.isVisible(true)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	private FortuneCategory initFortuneCategoryEntity(int i) {
		return FortuneCategory.builder()
			.description("short-desc" + i)
			.nameEn("test-name-en" + i)
			.name("test-name-kr" + i)
			.isVisible(true)
			.build();
	}
}
