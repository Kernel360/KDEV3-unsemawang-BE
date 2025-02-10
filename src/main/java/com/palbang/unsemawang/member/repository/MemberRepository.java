package com.palbang.unsemawang.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chemistry.dto.MemberWithDayGanDto;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
	Optional<Member> findByEmail(String email);

	@EntityGraph(attributePaths = {"id", "oauthId"})
	@Query("SELECT m FROM Member m WHERE m.oauthId = :oauthId")
	Optional<Member> findByOauthId(@Param("oauthId") String oauthId);

	//닉네임으로 회원 조회
	@Query("SELECT m FROM Member m WHERE m.nickname = :nickname AND m.isDeleted = false")
	Optional<Member> findByNickname(@Param("nickname") String nickname);

	Optional<Member> findById(String id);

	@Query("""
		    SELECT new com.palbang.unsemawang.chemistry.dto.MemberWithDayGanDto(m.id, f.dayGan)
		    FROM Member m
		    JOIN FortuneUserInfo f ON m.id = f.member.id
		    WHERE f.relation.id = 1
		""")
	List<MemberWithDayGanDto> findAllMembersWithDayGan();
}