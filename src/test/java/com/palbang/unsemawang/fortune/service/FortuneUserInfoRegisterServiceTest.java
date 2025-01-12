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
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRequestDto;
import com.palbang.unsemawang.fortune.dto.response.FortuneInfoRegisterResponseDto;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

@SpringBootTest(classes = FortuneInfoRegisterService.class)
class FortuneUserInfoRegisterServiceTest {

	@Autowired
	FortuneInfoRegisterService registerService;

	@MockBean
	MemberRepository memberRepository;

	@MockBean
	UserRelationRepository userRelationRepository;

	@MockBean
	FortuneUserInfoRepository fortuneUserInfoRepository;

	@Test
	@DisplayName("회원_사주정보_등록_성공테스트")
	void registerSuccess() {
		// Given
		FortuneInfoRequestDto fortuneInfoRequestDto = FortuneInfoRequestDto.builder()
			.memberId("aaa")
			.relationName("가족")
			.name("홍길동")
			.sex('남')
			.year(1990)
			.month(2)
			.day(12)
			.hour(14)
			.solunar("solar")
			.youn(0)
			.build();

		Member mockMember = Member.builder()
			.id("aaa")
			.build();

		UserRelation mockUserRelation = UserRelation.builder()
			.id(1L)
			.relationName("가족")
			.build();

		FortuneUserInfo savedUserInfo = FortuneUserInfo.builder()
			.member(mockMember)
			.relation(mockUserRelation)
			.nickname("홍길동")
			.year(1990)
			.month(2)
			.day(12)
			.birthtime(14)
			.sex('남')
			.build();

		when(memberRepository.findById("aaa")).thenReturn(Optional.of(mockMember));
		when(userRelationRepository.findByRelationName("가족")).thenReturn(Optional.of(mockUserRelation));
		when(fortuneUserInfoRepository.save(any(FortuneUserInfo.class))).thenReturn(savedUserInfo);

		// When
		FortuneInfoRegisterResponseDto result = registerService.registerFortuneInfo(fortuneInfoRequestDto);

		// Then
		assertNotNull(result);
		assertEquals("홍길동", result.getName());
		assertEquals('남', result.getSex()); // 변경: DTO와 일치하도록 검증
		assertEquals(1990, result.getYear());
		assertEquals(2, result.getMonth());
		assertEquals(12, result.getDay());
		verify(memberRepository, times(1)).findById("aaa");
		verify(userRelationRepository, times(1)).findByRelationName("가족");
		verify(fortuneUserInfoRepository, times(1)).save(any(FortuneUserInfo.class));
	}

	@Test
	@DisplayName("회원_사주정보_등록_실패테스트 - 회원 정보 없음")
	void registerFail_MemberNotFound() {
		// Given
		FortuneInfoRequestDto fortuneInfoRequestDto = FortuneInfoRequestDto.builder()
			.memberId("nonexistent") // 존재하지 않는 회원 ID
			.relationName("가족")
			.name("홍길동")
			.sex('남')
			.year(1990)
			.month(2)
			.day(12)
			.hour(14)
			.solunar("solar")
			.youn(0)
			.build();

		when(memberRepository.findById("nonexistent")).thenReturn(Optional.empty()); // 회원 없음

		// When & Then
		GeneralException exception = assertThrows(GeneralException.class,
			() -> registerService.registerFortuneInfo(fortuneInfoRequestDto));

		assertEquals("회원을 찾지 못했습니다.", exception.getMessage());
		verify(memberRepository, times(1)).findById("nonexistent");
		verify(userRelationRepository, times(0)).findByRelationName(anyString());
		verify(fortuneUserInfoRepository, times(0)).save(any(FortuneUserInfo.class));
	}

	@Test
	@DisplayName("회원_사주정보_등록_실패테스트 - 관계 정보 없음")
	void registerFail_RelationNotFound() {
		// Given
		FortuneInfoRequestDto fortuneInfoRequestDto = FortuneInfoRequestDto.builder()
			.memberId("aaa")
			.relationName("존재하지 않는 관계") // 존재하지 않는 관계명
			.name("홍길동")
			.sex('남')
			.year(1990)
			.month(2)
			.day(12)
			.hour(14)
			.solunar("solar")
			.youn(0)
			.build();

		Member mockMember = Member.builder()
			.id("aaa")
			.build();

		when(memberRepository.findById("aaa")).thenReturn(Optional.of(mockMember)); // 회원은 존재
		when(userRelationRepository.findByRelationName("존재하지 않는 관계")).thenReturn(Optional.empty()); // 관계 없음

		// When & Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> registerService.registerFortuneInfo(fortuneInfoRequestDto));

		assertEquals("존재하지 않는 관계입니다.", exception.getMessage());
		verify(memberRepository, times(1)).findById("aaa");
		verify(userRelationRepository, times(1)).findByRelationName("존재하지 않는 관계");
		verify(fortuneUserInfoRepository, times(0)).save(any(FortuneUserInfo.class));
	}
}