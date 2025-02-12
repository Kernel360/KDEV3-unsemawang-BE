package com.palbang.unsemawang.chemistry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface MemberMatchingScoreRepository extends JpaRepository<MemberMatchingScore, Long> {

	Optional<MemberMatchingScore> findByMemberAndMatchMember(Member member, Member matchMember);

	// 특정 점수 이상인 사용자 조회 (레디스 도입 대비)
	List<MemberMatchingScore> findByMemberIdAndScalingScoreGreaterThanEqualOrderByScalingScoreDesc(String memberId,
		int minScore);
}
