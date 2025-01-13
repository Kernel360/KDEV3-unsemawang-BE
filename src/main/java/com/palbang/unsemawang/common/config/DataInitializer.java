package com.palbang.unsemawang.common.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.FortuneCategoryRepository;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;

@Configuration
public class DataInitializer {
	@Bean
	public CommandLineRunner initData(
		FortuneCategoryRepository fortuneCategoryRepository,
		UserRelationRepository userRelationRepository
	) {
		return args -> {
			// FortuneCategory 중복 방지
			if (fortuneCategoryRepository.count() == 0) {
				// FortuneCategory 초기 데이터
				List<FortuneCategory> fortuneCategories = List.of(
					FortuneCategory.builder()
						.name("신점")
						.description("신점 상담 영역")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("타로")
						.description("타로 상담 영역")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("역학")
						.description("역학 상담 영역")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("심리")
						.description("심리 상담 영역")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build()
				);
				fortuneCategoryRepository.saveAll(fortuneCategories);
			}

			// UserRelation 중복 방지
			if (userRelationRepository.count() == 0) {
				// UserRelation 초기 데이터
				LocalDateTime now = LocalDateTime.now();
				List<UserRelation> userRelations = List.of(
					UserRelation.builder()
						.relationName("본인")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					UserRelation.builder()
						.relationName("가족")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					UserRelation.builder()
						.relationName("친구")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					UserRelation.builder()
						.relationName("미설정")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build()
				);
				userRelationRepository.saveAll(userRelations);
			}
		};
	}
}