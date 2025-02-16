package com.palbang.unsemawang.fortune.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;

@Repository
public interface FortuneCategoryRepository extends JpaRepository<FortuneCategory, Long> {
	List<FortuneCategory> findAllByIsVisibleIsTrue();
}
