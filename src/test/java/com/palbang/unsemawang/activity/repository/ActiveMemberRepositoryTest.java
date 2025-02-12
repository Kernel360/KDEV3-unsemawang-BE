package com.palbang.unsemawang.activity.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

@DataRedisTest
class ActiveMemberRepositoryTest {
	@Autowired
	private ActiveMemberRedisRepository activeMemberRepository;

	// @Test
	// @DisplayName("레디스 테스트 - 저장 후 조회")
	// public void saveAndGet() {
	// 	// given
	// 	ActiveMember memberActivity = ActiveMember.builder()
	// 		.memberId("test-user-id")
	// 		.build();
	//
	// 	activeMemberRepository.save(memberActivity);
	//
	// 	ActiveMember getMemberActivity = activeMemberRepository.findById(memberActivity.getMemberId())
	// 		.orElse(null);
	//
	// 	assertNotNull(getMemberActivity);
	// 	assertEquals(memberActivity.getMemberId(), getMemberActivity.getMemberId());
	//
	// 	activeMemberRepository.deleteById(memberActivity.getMemberId());
	// }
}