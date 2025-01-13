package com.palbang.unsemawang.member.repository;

import com.palbang.unsemawang.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = {"id","oauthId"})
    @Query("SELECT m FROM Member m WHERE m.oauthId = :oauthId")
    Optional<Member> findByOauthId(@Param("oauthId")String oauthId);

    //닉네임으로 회원 조회
    Optional<Member> findByNickname(String nickname);
}
