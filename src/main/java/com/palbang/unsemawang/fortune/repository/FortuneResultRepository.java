package com.palbang.unsemawang.fortune.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneResult;

@Repository
public interface FortuneResultRepository extends JpaRepository<FortuneResult, Long> {
}
