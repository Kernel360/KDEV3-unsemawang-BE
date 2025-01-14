package com.palbang.unsemawang.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.constant.MemberStatus;
import com.palbang.unsemawang.member.constant.OauthProvider;
import com.palbang.unsemawang.member.entity.Member;

@DataJpaTest
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("닉네임으로 회원 조회 - isDeleted가 false인 경우")
	void findByNickname_whenMemberExistsAndNotDeleted_shouldReturnMember() {
		// given
		Member member = createMember();
		memberRepository.save(member);

		// when
		Optional<Member> result = memberRepository.findByNickname("testNickname");

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getNickname()).isEqualTo("testNickname");
		assertThat(result.get().getIsDeleted()).isEqualTo(false);
	}

	@Test
	@DisplayName("닉네임으로 회원 조회 - isDeleted가 true인 경우")
	void findByNickname_whenMemberIsDeleted_shouldReturnEmpty() {
		// given
		Member member = createMember();

		memberRepository.save(member);

		// when
		Optional<Member> result = memberRepository.findByNickname("deletedNickname");

		// then
		assertThat(result).isNotPresent();
	}

	public Member createMember() {
		return Member.builder()
			.id("testUUID")
			.role(MemberRole.GENERAL) // 기본값 USER
			.email("test@example.com")
			.name("홍길동")
			.nickname("testNickname")
			.birthdate(LocalDate.of(1990, 1, 1))
			.phoneNumber("010-1234-5678")
			.profileUrl("http://example.com/profile.png")
			.point(100)
			.gender('M')
			.lastLoginAt(LocalDateTime.now())
			.changedAt(LocalDateTime.now())
			.memberStatus(MemberStatus.ACTIVE)
			.isDeleted(false)
			.isTermsAgreed(true)
			.oauthId("oauthId123")
			.oauthProvider(OauthProvider.GOOGLE) // 기본값 GOOGLE
			.isJoin(true)
			.password("password123")
			.address("서울특별시 강남구 테헤란로")
			.career("5년 경력")
			.shortBio("경험 많은 전문가입니다.")
			.detailBio("다양한 경력을 바탕으로 최고의 서비스를 제공합니다.")
			.build();
	}
}