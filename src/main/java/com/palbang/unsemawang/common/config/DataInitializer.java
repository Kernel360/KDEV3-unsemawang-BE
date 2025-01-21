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
						.name("새해신수")
						.nameEn("sinsu")
						.description("새해의 운세를 간단히 알아보세요")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("올해 토정비결")
						.nameEn("tojeong")
						.description("올해의 운세를 토정비결로 확인하세요")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("인생풀이")
						.nameEn("insaeng")
						.description("당신의 인생 전반에 대한 깊은 운세를 알려드립니다")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("사주운세")
						.nameEn("sajuunse")
						.description("사주를 통해 당신의 운명을 알아보세요")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("오늘의 운세")
						.nameEn("todayunse")
						.description("오늘 하루를 위한 특별한 운세를 확인하세요")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("정통궁합")
						.nameEn("gunghap")
						.description("당신과의 궁합을 깊이 분석합니다")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("현재 운세풀이")
						.nameEn("unsepuri")
						.description("현재 운세를 풀이해 드립니다")
						.isVisible(true)
						.isDeleted(false)
						.deletedAt(null)
						.build(),
					FortuneCategory.builder()
						.name("바이오리듬")
						.nameEn("biorhythm")
						.description("당신의 바이오리듬을 분석해보세요")
						.isVisible(false)
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
						.relationName("연인")
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