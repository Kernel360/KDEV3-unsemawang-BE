package com.palbang.unsemawang.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateMemberRequest {
	//닉네임
	@Pattern(
		regexp = "^[A-Za-z\\d가-힣]{2,10}$",
		message = "닉네임은 2~10자 이내의 문자(한글, 영어 대소문자, 숫자)여야 합니다."
	)
	@Schema(description = "닉네임", required = true, example = "운세마왕")
	private String nickname;
	//상세소개
	@Pattern(
		regexp = "^[a-zA-Z가-힣0-9!?()@.\\s]{0,100}$",
		message = "상세소개는 100자 이내로 작성해주세요. 특수문자는 !,?,(,),@,.만 사용 가능합니다."
	)
	@Schema(description = "상세소개", required = true, example = "매일 운세마왕 접속해서 운세 보는 사람입니다.")
	private String detailBio;

	@Schema(description = "매칭 동의 여부")
	private boolean isMatchAgreed;
}
