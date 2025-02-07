package com.palbang.unsemawang.chemistry.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chemistry.dto.response.ChemistryRecommendResponse;
import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {
	private final MemberMatchingScoreRepository memberMatchingScoreRepository;
	private final MemberRepository memberRepository;
	private final FortuneUserInfoRepository fortuneUserInfoRepository;

	@Transactional(readOnly = true)
	public List<ChemistryRecommendResponse> getTop5Matches(String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		List<MemberMatchingScore> matchingScores = memberMatchingScoreRepository.findTop5ByMemberIdOrderByScoreDesc(
			member.getId());

		if (matchingScores.isEmpty()) {
			throw new GeneralException(ResponseCode.ERROR_SEARCH);
		}

		// 최대 점수 찾기
		int maxScore = matchingScores.get(0).getScore(); // 가장 높은 점수 기준으로 스케일링
		// if (maxScore == 0) {
		// 	maxScore = 1; // 0 방지 (나누기 오류 방지)
		// }

		// ✅ 4. 상대방 matchMember.id를 이용해서 FortuneUserInfo에서 오행(dayGan) 가져오기
		return matchingScores.stream()
			.map(score -> {
				String matchMemberId = score.getMatchMember().getId();
				String dayGan = fortuneUserInfoRepository.findDayGanByMemberId(matchMemberId)
					.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH));

				return ChemistryRecommendResponse.from(score, dayGan, maxScore);
			})
			.collect(Collectors.toList());
	}
}