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
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
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
	private final FileService fileService;

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
		String imgUrl = fileService.getProfileImgUrl(memberId);
		
		return matchingScores.stream()
			.map(score -> {
				String matchMemberId = score.getMatchMember().getId();
				FortuneUserInfo fortuneUserInfo = fortuneUserInfoRepository.findByMemberIdRelationIdIsOne(matchMemberId)
					.orElseThrow(() -> new GeneralException(ResponseCode.ERROR_SEARCH));

				return ChemistryRecommendResponse.from(score, fortuneUserInfo, maxScore, imgUrl);
			})
			.collect(Collectors.toList());
	}
}