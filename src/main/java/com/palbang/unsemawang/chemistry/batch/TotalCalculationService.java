package com.palbang.unsemawang.chemistry.batch;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.chemistry.dto.MemberWithDayGanDto;
import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.chemistry.repository.MemberMatchingScoreRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TotalCalculationService {
	private final MemberMatchingScoreRepository scoreRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void calculateAndSaveChemistryScores() {
		List<MemberWithDayGanDto> members = memberRepository.findAllMembersWithDayGan();

		for (int i = 0; i < members.size(); i++) {
			MemberWithDayGanDto baseMemberDto = members.get(i);
			Member baseMember = memberRepository.findById(baseMemberDto.getMemberId())
				.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_ID));

			for (int j = 0; j < members.size(); j++) {
				if (i == j) {
					continue; // 자기 자신과 비교하지 않음
				}

				MemberWithDayGanDto targetMemberDto = members.get(j);
				Member targetMember = memberRepository.findById(targetMemberDto.getMemberId())
					.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_ID));

				int score = ChemistryCalculator.getChemistryScore(baseMemberDto.getDayGan(),
					targetMemberDto.getDayGan());

				// 기존 데이터가 있는지 확인
				MemberMatchingScore existingScore = scoreRepository.findByMemberAndMatchMember(baseMember,
					targetMember);

				if (existingScore != null) {
					// 기존 회원 → UPDATE
					existingScore.updateScore(score);
					scoreRepository.save(existingScore);
				} else {
					// 새로운 회원 → INSERT
					MemberMatchingScore newScore = MemberMatchingScore.builder()
						.member(baseMember)
						.matchMember(targetMember)
						.score(score)
						.build();
					scoreRepository.save(newScore);
				}
			}
		}
	}
}