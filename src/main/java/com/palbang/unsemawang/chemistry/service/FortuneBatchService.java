package com.palbang.unsemawang.chemistry.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FortuneBatchService {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final ChemistryCalculator calculator;
	private final RedisTemplate<String, Object> redisTemplate;
	private final MemberMatchingScoreRepository memberMatchingScoreRepository;

	private static final String REDIS_TOP20_KEY_PREFIX = "fortune:top20:"; // Redis 저장 키

	// 🔥 매일 3시 실행
	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void calculateFortuneScores() {
		log.info("🔥 [배치 시작] 궁합 점수 계산...");

		// 1️⃣ 모든 relation_id = 1 회원 조회
		List<FortuneUserInfo> users = fortuneUserInfoRepository.findByRelationId(1L);

		// 2️⃣ 모든 회원에 대해 궁합 점수 계산
		Map<Long, List<MemberMatchingScore>> scoreMap = new HashMap<>();
		for (FortuneUserInfo user1 : users) {
			List<MemberMatchingScore> userScores = new ArrayList<>();
			for (FortuneUserInfo user2 : users) {
				if (!user1.getId().equals(user2.getId())) {
					int score = FortuneBatchService.this.calculator.calculateCompatibility(user1.getDayGan(),
						user2.getDayGan());
					userScores.add(new MemberMatchingScore(user1.getId(), user2.getId(), score));
				}
			}

			// 3️⃣ 내림차순 정렬 후 Top 20만 저장
			userScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore())); // 점수 내림차순 정렬
			List<MemberMatchingScore> top20 = userScores.stream().limit(20).toList();
			scoreMap.put(user1.getId(), top20);
		}

		// 4️⃣ RDB 저장
		List<MemberMatchingScore> allScores = scoreMap.values().stream().flatMap(List::stream).toList();
		memberMatchingScoreRepository.saveAll(allScores);

		// 5️⃣ Redis 저장
		saveTop20ToRedis(scoreMap);

		log.info("✅ [배치 완료] 궁합 점수 계산 완료.");
	}

	// 🔹 Redis에 Top 20 저장
	private void saveTop20ToRedis(Map<Long, List<MemberMatchingScore>> scoreMap) {
		log.info("🗄 [Redis 업데이트] Top 20 저장...");

		for (Map.Entry<Long, List<MemberMatchingScore>> entry : scoreMap.entrySet()) {
			Long userId = entry.getKey();
			List<MemberMatchingScore> top20 = entry.getValue();

			String redisKey = REDIS_TOP20_KEY_PREFIX + userId;
			redisTemplate.delete(redisKey); // 기존 데이터 삭제
			redisTemplate.opsForList().rightPushAll(redisKey, top20);
		}
	}

	// 🔄 특정 회원 Top 5 랜덤 추출 (API에서 사용)
	public List<MemberMatchingScore> getRandomTop5Matches(Long userId) {
		String redisKey = REDIS_TOP20_KEY_PREFIX + userId;
		List<Object> top20 = redisTemplate.opsForList().range(redisKey, 0, 19);

		if (top20 == null || top20.isEmpty()) {
			return Collections.emptyList(); // 데이터가 없으면 빈 리스트 반환
		}

		// 🔀 무작위 5개 선택
		Collections.shuffle(top20);
		return top20.stream().limit(5).map(obj -> (MemberMatchingScore)obj).toList();
	}
}

