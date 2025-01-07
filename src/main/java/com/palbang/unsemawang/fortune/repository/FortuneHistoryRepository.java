package com.palbang.unsemawang.fortune.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneHistory;

@Repository
public interface FortuneHistoryRepository extends JpaRepository<FortuneHistory, Long> {
}
