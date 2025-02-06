package com.palbang.unsemawang.common.config.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.chemistry.service.ChemistryCalculator;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MatchingScoreBatchJob {

	private final EntityManagerFactory entityManagerFactory;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final MemberMatchingScoreRepository memberMatchingScoreRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job memberMatchingScoreJob(JobRepository jobRepository) {
		return new JobBuilder("memberMatchingScoreJob", jobRepository)
			.start(calculateScoreStep(jobRepository))
			.build();
	}

	@Bean
	public Step calculateScoreStep(JobRepository jobRepository) {
		return new StepBuilder("calculateScoreStep", jobRepository)
			.<FortuneUserInfo, List<MemberMatchingScore>>chunk(10, transactionManager) // 한 번에 10명씩 처리
			.reader(fortuneUserInfoReader())
			.processor(chemistryScoreProcessor())
			.writer(matchingScoreWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<FortuneUserInfo> fortuneUserInfoReader() {
		return new JpaPagingItemReaderBuilder<FortuneUserInfo>()
			.name("fortuneUserInfoReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("SELECT f FROM FortuneUserInfo f WHERE f.relation.id = 1")
			.pageSize(10)
			.build();
	}

	@Bean
	public ItemProcessor<FortuneUserInfo, List<MemberMatchingScore>> chemistryScoreProcessor() {
		return user -> {
			List<FortuneUserInfo> allUsers = fortuneUserInfoRepository.findByRelationId(1L);
			List<MemberMatchingScore> scores = new ArrayList<>();

			for (FortuneUserInfo matchUser : allUsers) {
				if (!user.getId().equals(matchUser.getId())) {
					int score = ChemistryCalculator.getChemistryScore(user.getDayGan(), matchUser.getDayGan());
					MemberMatchingScore matchingScore = MemberMatchingScore.builder()
						.memberId(user.getMember())
						.matchMemberId(matchUser.getMember())
						.score(score)
						.build();
					scores.add(matchingScore);
				}
			}
			return scores;
		};
	}

	@Bean
	public CompositeItemWriter<List<MemberMatchingScore>> matchingScoreWriter() {
		CompositeItemWriter<List<MemberMatchingScore>> writer = new CompositeItemWriter<>();
		writer.setDelegates(List.of(new ListItemWriter<>()));
		return writer;
	}
}