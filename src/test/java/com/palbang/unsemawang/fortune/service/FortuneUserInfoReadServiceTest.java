package com.palbang.unsemawang.fortune.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoReadResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;

@SpringBootTest(classes = FortuneUserInfoReadService.class)
class FortuneUserInfoReadServiceTest {
	@Autowired
	private FortuneUserInfoReadService readService;

	@MockBean
	private FortuneUserInfoRepository fortuneUserInfoRepository;

	@Test
	@DisplayName("특정 사용자 ID로 사주 정보 조회 성공 테스트")
	void fortuneInfoByMemberId_success() {
		// Given
		String memberId = "aaa";

		UserRelation mockUserRelation = UserRelation.builder()
			.relationName("가족")
			.build();

		FortuneUserInfo mockFortuneUserInfo = FortuneUserInfo.builder()
			.nickname("홍길동")
			.sex('남')
			.year(2020)
			.month(2)
			.day(2)
			.birthtime(2)
			.relation(mockUserRelation)
			.solunar("solar")
			.youn(0)
			.build();

		when(fortuneUserInfoRepository.findByMemberId(memberId))
			.thenReturn(List.of(mockFortuneUserInfo));

		// When
		List<FortuneUserInfoReadResponseDto> result = readService.fortuneInfoListRead(memberId);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("가족", result.get(0).getRelationName());
		assertEquals("홍길동", result.get(0).getName());
		assertEquals('남', result.get(0).getSex());
		assertEquals(2020, result.get(0).getYear());
		assertEquals(2, result.get(0).getMonth());
		assertEquals(2, result.get(0).getDay());
		assertEquals("solar", result.get(0).getSolunar());
		assertEquals(0, result.get(0).getYoun());

		verify(fortuneUserInfoRepository, times(1)).findByMemberId(memberId);
	}

	@Test
	@DisplayName("특정 사용자 ID로 사주 정보 조회 실패 테스트 - 데이터 없음")
	void fortuneInfoByMemberId_fail_noData() {
		// Given
		String memberId = "unknown";

		when(fortuneUserInfoRepository.findByMemberId(memberId))
			.thenReturn(List.of());

		// When & Then
		GeneralException exception = assertThrows(GeneralException.class, () -> {
			readService.fortuneInfoListRead(memberId);
		});

		assertEquals("해당 회원의 사주 정보를 찾을 수 없습니다.", exception.getMessage());

		verify(fortuneUserInfoRepository, times(1)).findByMemberId(memberId);
	}

	@Test
	@DisplayName("특정 사용자 ID로 사주 정보 조회 성공 테스트 - 여러 데이터")
	void fortuneInfoByMemberId_multipleEntries() {
		// Given
		String memberId = "aaa";

		UserRelation relation1 = UserRelation.builder()
			.relationName("가족")
			.build();

		UserRelation relation2 = UserRelation.builder()
			.relationName("친구")
			.build();

		FortuneUserInfo fortuneInfo1 = FortuneUserInfo.builder()
			.nickname("홍길동")
			.sex('남')
			.year(2020)
			.month(2)
			.day(2)
			.birthtime(2)
			.relation(relation1)
			.solunar("solar")
			.youn(0)
			.build();

		FortuneUserInfo fortuneInfo2 = FortuneUserInfo.builder()
			.nickname("김철수")
			.sex('남')
			.year(1965)
			.month(6)
			.day(15)
			.birthtime(2)
			.relation(relation2)
			.solunar("lunar")
			.youn(1)
			.build();

		when(fortuneUserInfoRepository.findByMemberId(memberId))
			.thenReturn(List.of(fortuneInfo1, fortuneInfo2));

		// When
		List<FortuneUserInfoReadResponseDto> result = readService.fortuneInfoListRead(memberId);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());

		// 첫 번째 데이터 검증
		assertEquals("가족", result.get(0).getRelationName());
		assertEquals("홍길동", result.get(0).getName());
		assertEquals('남', result.get(0).getSex());
		assertEquals(2020, result.get(0).getYear());
		assertEquals(2, result.get(0).getMonth());
		assertEquals(2, result.get(0).getDay());
		assertEquals("solar", result.get(0).getSolunar());
		assertEquals(0, result.get(0).getYoun());

		// 두 번째 데이터 검증
		assertEquals("친구", result.get(1).getRelationName());
		assertEquals("김철수", result.get(1).getName());
		assertEquals('남', result.get(1).getSex());
		assertEquals(1965, result.get(1).getYear());
		assertEquals(6, result.get(1).getMonth());
		assertEquals(15, result.get(1).getDay());
		assertEquals("lunar", result.get(1).getSolunar());
		assertEquals(1, result.get(1).getYoun());

		verify(fortuneUserInfoRepository, times(1)).findByMemberId(memberId);
	}

}

