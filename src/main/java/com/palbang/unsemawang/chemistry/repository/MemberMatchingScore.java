package com.palbang.unsemawang.chemistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMatchingScore extends JpaRepository<MemberMatchingScore, Long> {
}
