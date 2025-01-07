package com.palbang.unsemawang.fortune.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.palbang.unsemawang.fortune.entity.FortuneContent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FortuneContentRepositoryTest {
	@Autowired
	FortuneContentRepository fortuneContentRepository;

	/**
	 *
	 *  [ 삽입 테스트 ]
	 *  1. 성공 - 기본 값 제외 필수 필드 포함 삽입
	 *  2. 성공 - 필수 필드 포함 삽입
	 *  3. 실패 - 필수 필드 미포함 삽입
	 *
	 *  [ 조회 테스트 ]
	 *  1. 성공 - 10개 데이터 넣고 10개 조회되는지 확인
	 *
	 *  [ 수정 테스트 ]
	 *  1. 성공 - 필수 필드 포함 수정
	 *  2. 실패 - 필수 필드 미포함 수정
	 *
	 *  [ 삭제 테스트 ]
	 *  1. 성공 - 존재하는 컬럼 삭제
	 */

	/* 삽입 테스트 */
	@Test
	@DisplayName("삽입 테스트 | 성공 - 기본 값 제외 필수 필드 포함 삽입 ")
	public void insertContent_notDefaultField() {
		// 1. given - 필수 필드로만 구성된 엔티티 객체 생성(기본 값 세팅된 필드 제외)
		FortuneContent fortuneContent = FortuneContent.builder()
			.fortuneContentName("test-name")
			.description("test-desc")
			.path("/test-path")
			.build();

		// 2. when - 엔티티 저장
		FortuneContent savedFortuneContent = fortuneContentRepository.save(fortuneContent);
		assertNotNull(savedFortuneContent.getId());

		// 3. then - 조회된 엔티티에 삭제 일시를 제외한 모든 필드에 값이 들어가 있어야 한다
		FortuneContent getFortuneContent = fortuneContentRepository.findAll().get(0);
		assertNotNull(getFortuneContent);
		assertTrue(getFortuneContent.getIsActive());
		assertFalse(getFortuneContent.getIsDeleted());
		assertNotNull(getFortuneContent.getRegisteredAt());

	}

	@Test
	@DisplayName("삽입 테스트 | 성공 - 필수 필드 포함 삽입 ")
	public void insertContent_allEssentialField() {
		// 1. given - 필수 필드로만 구성된 엔티티 객체 생성
		FortuneContent fortuneContent = FortuneContent.builder()
			.fortuneContentName("test-name")
			.description("test-desc")
			.path("/test-path")
			.isActive(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		// 2. when - 엔티티 저장
		FortuneContent savedFortuneContent = fortuneContentRepository.save(fortuneContent);
		assertNotNull(savedFortuneContent.getId());

		// 3. then - 조회한 데이터와 삽입한 데이터가 일치해야한다
		FortuneContent getFortuneContent = fortuneContentRepository.findAll().get(0);
		assertNotNull(getFortuneContent);
		assertEquals(fortuneContent.getFortuneContentName(), savedFortuneContent.getFortuneContentName());
		assertEquals(fortuneContent.getIsActive(), savedFortuneContent.getIsActive());

	}

	@Test
	@DisplayName("삽입 테스트 | 실패 - 필수 필드 미포함 삽입 ")
	public void insertContent_notEssentialField() {
		// 1. given - 일부 필수 필드 미포함하여 엔티티 생성
		FortuneContent fortuneContent = FortuneContent.builder()
			.fortuneContentName(null)
			.description("test-desc")
			.path("/test-path")
			.isActive(false)
			.isDeleted(true)
			.registeredAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		// 2. when, then - 엔티티 저장할 때 예외가 발생해야 한다
		assertThrows(DataIntegrityViolationException.class,
			() -> fortuneContentRepository.save(fortuneContent));

	}

	/**
	 *	 [ 조회 테스트 ]
	 * 	 1. 성공 - 10개 데이터 넣고 10개 조회되는지 확인
	 */
	@Test
	@DisplayName("조회 테스트 | 성공 - 10개 데이터 넣고 10개 조회되는지 확인")
	public void findTest_all() {
		// 1. given - 10개 엔티티 생성
		int cnt = 10;
		List<FortuneContent> fortuneContentList = new ArrayList<>();
		for (int i = 0; i < cnt; i++) {
			fortuneContentList.add(createFortuneContent(i));
		}

		// 2. when - 10개 엔티티 삽입
		fortuneContentRepository.saveAll(fortuneContentList);

		// 3. then - 전체 조회 결과 리스트가 삽입한 엔티티와 동일해야한다
		List<FortuneContent> findList = fortuneContentRepository.findAll();
		assertEquals(cnt, findList.size());
		assertTrue(findList.containsAll(fortuneContentList));

	}

	/* 헬퍼 */
	private FortuneContent createFortuneContent(int i) {
		return FortuneContent.builder()
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