package com.palbang.unsemawang.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

@SpringBootTest(classes = MemberService.class)
class MemberServiceTest {

	@MockBean
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("닉네임 중복 - 닉네임이 이미 존재하면 GeneralException 발생")
	void duplicateNicknameCheck_Exist() {
		// given
		String existingNickname = "testNickname";
		when(memberRepository.findByNickname(existingNickname)).thenReturn(Optional.of(mock(Member.class)));

		// when & then
		assertThatExceptionOfType(GeneralException.class)
			.isThrownBy(() -> memberService.duplicateNicknameCheck(existingNickname))
			.withMessage(ResponseCode.DUPLICATED_VALUE.getMessage());

		verify(memberRepository, times(1)).findByNickname(existingNickname);
	}

	@Test
	@DisplayName("닉네임 중복 없음 - 닉네임이 존재하지 않으면 예외 없음")
	void duplicateNicknameCheck_whenNicknameDoesNotExist_shouldPass() {
		// given
		String newNickname = "uniqueNickname";
		when(memberRepository.findByNickname(newNickname)).thenReturn(Optional.empty());

		// when
		memberService.duplicateNicknameCheck(newNickname);

		// then
		verify(memberRepository, times(1)).findByNickname(newNickname);
	}
}