package com.palbang.unsemawang.chemistry.dto.response;

import java.time.LocalDate;

import com.palbang.unsemawang.chemistry.constant.FiveElements;
import com.palbang.unsemawang.chemistry.entity.MemberMatchingScore;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChemistryRecommendResponse {

	@Schema(required = true, description = "나와의 궁합 매칭 점수")
	private Integer score;

	@Schema(required = true, description = "상대방의 오행", example = "木")
	private String fiveElementCn;

	@Schema(required = true, description = "상대방 닉네임")
	private String nickname;

	@Schema(required = true, description = "상대방 프로필 이미지 url")
	private String profileImageUrl;

	@Schema(required = true, description = "상대방 프로필 소개")
	private String profileBio;

	@Schema(required = true, description = "상대방 성별")
	private char sex;

	@Schema(required = false, description = "상대방 마지막 로그인 날짜")
	private LocalDate lastActiveDate;

	// 점수 스케일링 포함된 정적 팩토리 메서드
	public static ChemistryRecommendResponse from(MemberMatchingScore scoreEntity, FortuneUserInfo fortuneUserInfo,
		int maxScore, String imgUrl) {
		Member matchMember = scoreEntity.getMatchMember();
		int scaledScore = (int)Math.round(((double)scoreEntity.getScore() / maxScore) * 10);

		String element = FiveElements.convertToChinese(fortuneUserInfo.getDayGan());

		return ChemistryRecommendResponse.builder()
			.score(scaledScore)
			.fiveElementCn(element)
			.nickname(matchMember.getNickname())
			.profileImageUrl(imgUrl)
			.profileBio(matchMember.getDetailBio())
			.sex(fortuneUserInfo.getSex())
			.lastActiveDate(matchMember.getLastLoginAt() != null ? matchMember.getLastLoginAt().toLocalDate() : null)
			.build();
	}
}