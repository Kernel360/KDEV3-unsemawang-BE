package com.palbang.unsemawang.fortune.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.palbang.unsemawang.config.QueryDslConfig;
import com.palbang.unsemawang.fortune.entity.FortuneContent;

@DataJpaTest
@Import(QueryDslConfig.class)
public class FortuneContentSearchRepositoryTest {

	@Autowired
	private FortuneContentRepository fortuneContentRepository;

	/*
	 *  [ 검색 테스트 ]
	 *  - 검색 키워드가 영문표기명에 포함된 엔티티 1개 조회
	 *  - 검색 키워드가 한글표기명에 포함된 엔티티 1개 조회
	 *  - 검색 키워드가 없는 경우 빈 리스트 조회
	 * */

	@Test
	@DisplayName("검색 - 검색 키워드가 영문표기명에 포함된 엔티티 1개 조회")
	public void search_keywordInNameEn() {
		// 1. given - 컨텐츠 삽입
		FortuneContent fortuneContent1 = FortuneContent.builder()
			.nameKo("한글테스트명검색되어라")
			.nameEn("english-test-name-search-please")
			.build();

		FortuneContent fortuneContent2 = FortuneContent.builder()
			.nameKo("한글한글한글")
			.nameEn("english-test-name")
			.build();

		List<FortuneContent> savedList = fortuneContentRepository.saveAll(List.of(fortuneContent1, fortuneContent2));
		assertNotNull(savedList);

		// 1-1. 검색 키워드 값 초기화
		String searchKeyword = "search";

		// 2. when - 키워드가 영문 또는 한글 표기명에 포함된 리스트 조회
		List<FortuneContent> findList = fortuneContentRepository.findByKeyword(searchKeyword);

		// 3. then - 결과 개수가 1개여야함. fortuneContent1 엔티티와 값이 같아야함
		assertEquals(1, findList.size());
		assertEquals(fortuneContent1.getNameEn(), findList.get(0).getNameEn());
		assertEquals(fortuneContent1.getNameKo(), findList.get(0).getNameKo());

	}

	@Test
	@DisplayName("검색 - 검색 키워드가 한글표기명에 포함된 엔티티 1개 조회")
	public void search_keywordInNameKr() {
		// 1. given - 컨텐츠 삽입
		FortuneContent fortuneContent1 = FortuneContent.builder()
			.nameKo("한글테스트명검색되어라리리라랄")
			.nameEn("english-test-name-search-please")
			.build();

		FortuneContent fortuneContent2 = FortuneContent.builder()
			.nameKo("테스트검색이것이검색될것이다")
			.nameEn("english-test-name")
			.build();

		List<FortuneContent> savedList = fortuneContentRepository.saveAll(List.of(fortuneContent1, fortuneContent2));
		assertNotNull(savedList);

		// 1-1. 검색 키워드 값 초기화
		String searchKeyword = "이다";

		// 2. when - 키워드가 영문 또는 한글 표기명에 포함된 리스트 조회
		List<FortuneContent> findList = fortuneContentRepository.findByKeyword(searchKeyword);

		// 3. then - 결과 개수가 1개여야함. fortuneContent1 엔티티와 값이 같아야함
		assertEquals(1, findList.size());
		assertEquals(fortuneContent2.getNameEn(), findList.get(0).getNameEn());
		assertEquals(fortuneContent2.getNameKo(), findList.get(0).getNameKo());

	}

	@Test
	@DisplayName("검색 - 검색 키워드가 없는 경우 빈 리스트 조회")
	public void search_notExistKeyword() {

		// 1-1. 검색 키워드 값 초기화
		String searchKeyword = "";

		// 2. when - 키워드가 영문 또는 한글 표기명에 포함된 리스트 조회
		List<FortuneContent> findList = fortuneContentRepository.findByKeyword(searchKeyword);

		// 3. then - 결과 개수가 1개여야함. fortuneContent1 엔티티와 값이 같아야함
		assertEquals(0, findList.size());

	}
}
