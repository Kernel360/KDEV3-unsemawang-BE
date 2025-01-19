package com.palbang.unsemawang.member.dto;

import com.palbang.unsemawang.member.constant.MemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileDto {
	@Schema(description = "회원 권한", required = true)
	private MemberRole role;
	@Schema(description = "회원 이메일", required = true)
	private String email;
	@Schema(description = "회원 닉네임", required = true)
	private String nickname;
	@Schema(description = "회원 가입여부", required = true)
	private Boolean isJoin;
	@Schema(description = "회원 프로필 URL", required = true)
	private String profileUrl;
	@Schema(description = "회원보유 포인트", required = true)
	private int point;
}
