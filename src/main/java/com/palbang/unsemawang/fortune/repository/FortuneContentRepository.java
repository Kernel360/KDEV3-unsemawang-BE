package com.palbang.unsemawang.fortune.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneContent;

@Repository
public interface FortuneContentRepository extends JpaRepository<FortuneContent, Long> {

	@Query("SELECT fc FROM FortuneContent fc WHERE (fc.nameEn LIKE %:keyword% OR fc.nameKo LIKE %:keyword%) AND fc.isVisible = true")
	List<FortuneContent> findByKeyword(String keyword);

	@Query("SELECT fc FROM FortuneContent fc WHERE fc.fortuneCategory.nameEn = :categoryName AND fc.isVisible = true")
	List<FortuneContent> findAllByFortuneCategory(String categoryName);

	@Query("SELECT fc FROM FortuneContent fc " +
		"WHERE (fc.nameEn LIKE %:keyword% OR fc.nameKo LIKE %:keyword%) " +
		"AND (fc.fortuneCategory.nameEn = :categoryName OR fc.fortuneCategory.name = :categoryName)" +
		"AND fc.isVisible = true")
	List<FortuneContent> findByKeywordAndFortuneCategory(String keyword, String categoryName);
}
