package com.palbang.unsemawang.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.palbang.unsemawang.config.QueryDslConfig;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.entity.Member;

@DataJpaTest
@Import(QueryDslConfig.class)
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("닉네임으로 회원 조회 - isDeleted가 false인 경우")
	void findByNickname_whenMemberExistsAndNotDeleted_shouldReturnMember() {
		// given
		Member member = createMember("testNickname", false);

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
		Member member = createMember("deletedNickname", true);

		memberRepository.save(member);

		// when
		Optional<Member> result = memberRepository.findByNickname("deletedNickname");

		// then
		assertThat(result).isNotPresent();
	}

	public Member createMember(String nickname, boolean isDeleted) {
		return Member.builder()
			.id("testUUID")
			.role(MemberRole.GENERAL)
			.email("test@example.com")
			.nickname(nickname)
			.isDeleted(isDeleted)
			.build();
	}
}