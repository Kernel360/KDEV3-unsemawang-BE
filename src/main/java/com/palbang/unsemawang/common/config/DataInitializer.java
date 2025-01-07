package com.palbang.unsemawang.common.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.palbang.unsemawang.fortune.entity.UserRelation;
import com.palbang.unsemawang.fortune.repository.UserRelationRepository;
import com.palbang.unsemawang.member.entity.FortuneCategory;
import com.palbang.unsemawang.member.repository.FortuneCategoryRepository;

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
					// 가족 관계
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("할머니")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("할아버지")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("아버지")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("어머니")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("배우자")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("아들")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("딸")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("형제")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("가족")
						.relationDetailName("자매")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					// 회사 관계
					UserRelation.builder()
						.relationName("회사")
						.relationDetailName("선배")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("회사")
						.relationDetailName("동기")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("회사")
						.relationDetailName("후배")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					// 친구 관계
					UserRelation.builder()
						.relationName("친구")
						.relationDetailName("애인")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("친구")
						.relationDetailName("친구")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),

					// 미설정 관계
					UserRelation.builder()
						.relationName("미설정")
						.relationDetailName("선택안함")
						.registeredAt(now)
						.updatedAt(now)
						.isDeleted(false)
						.build(),
					UserRelation.builder()
						.relationName("미설정")
						.relationDetailName("기타")
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
