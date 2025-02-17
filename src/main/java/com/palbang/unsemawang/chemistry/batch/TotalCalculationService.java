package com.palbang.unsemawang.chemistry.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chemistry.dto.MemberWithDayGanDto;
import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalCalculationService {
	private final MemberMatchingScoreRepository scoreRepository;
	private final MemberRepository memberRepository;
	private final StringRedisTemplate redisTemplate;

	private static final String REDIS_KEY_PREFIX = "matching_status:"; // Redis 키 prefix
	private static final long EXPIRATION_TIME = 10; // 상태 유지 시간 (10분)

	private static final int MAX_SCORE = 8;
	private static final int MIN_SCORE = -9;

	private static final double MAX_WEIGHT = 1.2;  // 최근 활동한 경우 최대 1.2배
	private static final double MIN_WEIGHT = 0.8;  // 비활동 시 최소 0.8배
	private static final int DAYS_THRESHOLD = 7;  // 7일을 기준으로 점진적으로 감소

	@Transactional
	public void runDailyChemistryScoreBatchAt4AM() {

		List<MemberWithDayGanDto> members = memberRepository.findAllMembersWithDayGan();

		for (MemberWithDayGanDto baseDto : members) {
			Member baseMember = getValidGeneralMember(baseDto.getMemberId());

			for (MemberWithDayGanDto targetDto : members) {
				if (baseDto.getMemberId().equals(targetDto.getMemberId())) {
					continue;
				}

				Member targetMember = getValidGeneralMember(targetDto.getMemberId());

				int baseScore = ChemistryCalculator.getChemistryScore(baseDto.getDayGan(), targetDto.getDayGan());
				int scalingScore = applyWeightAndScaleScore(baseScore, targetMember.getLastActivityAt());

				saveOrUpdateMatchingScore(baseMember, targetMember, baseScore, scalingScore);
			}
		}
	}

	/**
	 * 회원 1명에 대한 점수 계산 로직
	 */
	@Async
	@Transactional
	public void calculateAndSaveChemistryScoresForNewMember(String newMemberId) {
		log.info("궁합 점수 계산 시작 - 회원 ID: {}", newMemberId);

		// Redis에 "processing" 상태 저장 (10분 후 자동 만료)
		redisTemplate.opsForValue()
			.set(REDIS_KEY_PREFIX + newMemberId, "processing", EXPIRATION_TIME, TimeUnit.MINUTES);

		try {
			Member newMember = getValidGeneralMember(newMemberId);

			MemberWithDayGanDto newMemberDayGan = memberRepository.findByMemberWithDayGan(newMemberId)
				.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_TENGAN));

			List<MemberWithDayGanDto> existingMembers = memberRepository.findAllMembersWithDayGan();

			for (MemberWithDayGanDto existingMemberDto : existingMembers) {
				if (existingMemberDto.getMemberId().equals(newMemberId)) {
					continue;
				}

				Member existingMember = getValidGeneralMember(existingMemberDto.getMemberId());

				int baseScore = ChemistryCalculator.getChemistryScore(newMemberDayGan.getDayGan(),
					existingMemberDto.getDayGan());
				int scalingScore = applyWeightAndScaleScore(baseScore, existingMember.getLastActivityAt());

				saveOrUpdateMatchingScore(newMember, existingMember, baseScore, scalingScore);
			}

			// 작업 완료 후 Redis에 "completed" 저장
			redisTemplate.opsForValue()
				.set(REDIS_KEY_PREFIX + newMemberId, "completed", EXPIRATION_TIME, TimeUnit.MINUTES);

			log.info("궁합 점수 계산 완료 - 회원 ID: {}", newMemberId);

		} catch (Exception e) {
			// 오류 발생 시 Redis 상태 삭제
			redisTemplate.delete(REDIS_KEY_PREFIX + newMemberId);
			throw new GeneralException(ResponseCode.MATCHING_ERROR);
		}
	}

	// 헬퍼 메서드

	/**
	 * 회원 ID로 GENERAL 회원을 조회하고 검증
	 */
	private Member getValidGeneralMember(String memberId) {
		return memberRepository.findById(memberId)
			.filter(member -> member.getRole().equals(MemberRole.GENERAL))
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_ID));
	}

	/**
	 * 기존 점수 업데이트 or 신규 점수 추가
	 * @param baseMember 기준 멤버
	 * @param targetMember 비교 멤버
	 * @param baseScore 원점수
	 * @param scalingScore 100점 만점 스케일링 점수
	 */
	private void saveOrUpdateMatchingScore(Member baseMember, Member targetMember, int baseScore, int scalingScore) {
		scoreRepository.findByMemberAndMatchMember(baseMember, targetMember)
			.ifPresentOrElse(
				existingScore -> updateExistingScore(existingScore, baseScore, scalingScore), // 업데이트 메서드 호출
				() -> saveNewMatchingScore(baseMember, targetMember, baseScore, scalingScore) // 신규 저장 메서드 호출
			);
	}

	/**
	 * MemberMatchingScore 객체 생성 & 저장
	 * @param baseMember 기준 멤버
	 * @param targetMember 비교 멤버
	 * @param baseScore 원점수
	 * @param scalingScore 100점 만점 스케일링 점수
	 */
	private void saveNewMatchingScore(Member baseMember, Member targetMember, int baseScore, int scalingScore) {
		scoreRepository.save(
			MemberMatchingScore.builder()
				.member(baseMember)
				.matchMember(targetMember)
				.score(baseScore)
				.scalingScore(scalingScore)
				.build()
		);
	}

	/**
	 * 기존 MemberMatchingScore 객체의 점수 업데이트  (JPA 변경 감지로 save() 호출 필요 없음)
	 * @param existingScore 점수를 업데이트 할 기존 객체
	 * @param baseScore 원점수
	 * @param scalingScore 100점 만점 스케일링 점수
	 */
	private void updateExistingScore(MemberMatchingScore existingScore, int baseScore, int scalingScore) {
		existingScore.updateScore(baseScore, scalingScore);
	}

	/**
	 * 최소-최대 정규화 (Min-Mas Scaling)
	 * @param score 원본 점수 (-9 ~ 8)
	 * @return 100점 만점 스케일링된 점수 (0 ~ 100)
	 */
	private int scaleScore(int score) {
		int scaled = (int)(((double)(score - MIN_SCORE) / (MAX_SCORE - MIN_SCORE)) * 100);
		return Math.min(100, Math.max(0, scaled)); // 0~100 범위 유지
	}

	/**
	 * 최근 활동 일시를 기준으로 가중치 계산
	 * 최근 활동일수록 가중치 비율이 커짐: MAX_WEIGHT = 1.2
	 * @param targetActivity : targetMember의 최근 활동 일시
	 * @return 가중치 비율
	 */
	private double calculateActivityWeight(LocalDateTime targetActivity) {
		LocalDateTime now = LocalDateTime.now();
		long targetDaysAgo = targetActivity == null ? DAYS_THRESHOLD : Duration.between(targetActivity, now).toDays();

		// 최신 활동일일수록 가중치 증가
		return Math.max(MIN_WEIGHT,
			MAX_WEIGHT - (targetDaysAgo / (double)DAYS_THRESHOLD) * (MAX_WEIGHT - MIN_WEIGHT));
	}

	/**
	 * 가중치를 적용한 최종 점수 계산 후, 100점 만점으로 스케일링
	 */
	private int applyWeightAndScaleScore(int baseScore, LocalDateTime lastActivityAt) {
		double weight = calculateActivityWeight(lastActivityAt);
		int finalScore = (int)(baseScore * weight);
		return scaleScore(finalScore);
	}

}