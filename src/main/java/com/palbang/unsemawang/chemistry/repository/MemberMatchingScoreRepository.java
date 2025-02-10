package com.palbang.unsemawang.chemistry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface MemberMatchingScoreRepository extends JpaRepository<MemberMatchingScore, Long> {

	List<MemberMatchingScore> findTop4ByMemberIdOrderByScoreDesc(String memberId);

	MemberMatchingScore findByMemberAndMatchMember(Member member, Member matchMember);
}
