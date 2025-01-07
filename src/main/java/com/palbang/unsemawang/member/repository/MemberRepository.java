package com.palbang.unsemawang.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
