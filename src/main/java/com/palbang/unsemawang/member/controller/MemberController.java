package com.palbang.unsemawang.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequest;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoRegisterService;
import com.palbang.unsemawang.member.dto.MemberProfileDto;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;
import com.palbang.unsemawang.member.service.MemberService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final FortuneUserInfoRegisterService fortuneInfoRegisterService;

	@Operation(
		summary = "회원 닉네임 중복 체크",
		description = "회원가입시 닉네임 중복 체크를 진행합니다",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@GetMapping("/check-nickname")
	public ResponseEntity<Void> checkNickname(
		@Pattern(
			regexp = "^[A-Za-z\\d가-힣_]{2,15}$",
			message = "닉네임은 2~15자 이내의 문자(한글, 영어 대소문자, 숫자, 언더바)여야 합니다."
		)
		String nickname) {

		memberService.duplicateNicknameCheck(nickname);

		return ResponseEntity.ok().build();
	}

	@Operation(
		description = "회원가입 과정 중 추가정보 입력을 위한 api 입니다. 인증 토큰이 담긴 쿠키를 직접 보내셔야 테스트가 가능합니다!",
		summary = "회원가입 - 추가정보 입력"
	)
	// 회원 가입 추가정보 입력
	@PostMapping("/signup/extra-info")
	public ResponseEntity<Response> signupExtraInfo(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody SignupExtraInfoRequest signupExtraInfo
	) {
		// 인증 객체로부터 회원 id 값 가져와 세팅
		signupExtraInfo.updateMemberId(auth.getId());

		// 추가 정보 업데이트
		memberService.signupExtraInfo(signupExtraInfo);

		// 사주 정보 등록
		fortuneInfoRegisterService.registerFortuneInfo(FortuneInfoRegisterRequest.from(signupExtraInfo));

		return ResponseEntity.ok(
			Response.success(ResponseCode.SUCCESS_INSERT)
		);
	}

	@Operation(
		summary = "회원 정보 조회 api",
		description = "회원 정보를 조회합니다",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@GetMapping("/profile")
	public ResponseEntity<MemberProfileDto> getProfile(
		@AuthenticationPrincipal CustomOAuth2User auth) {
		//회원정보에 해당하는 프로필 조회
		MemberProfileDto memberProfile = memberService.getMemberProfile(auth.getId());

		return ResponseEntity.ok(memberProfile);
	}

}