package com.palbang.unsemawang.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequest;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoRegisterService;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;

import org.springframework.http.HttpStatus;
import com.palbang.unsemawang.member.service.MemberService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

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

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// 회원 가입 추가정보 입력
	@PostMapping("/signup/extra-info")
	public ResponseEntity<Response> signupExtraInfo(@Valid @RequestBody SignupExtraInfoRequest signupExtraInfo) {

		// 추가 정보 업데이트
		memberService.signupExtraInfo(signupExtraInfo);

		// 사주 정보 등록
		fortuneInfoRegisterService.registerFortuneInfo(FortuneInfoRegisterRequest.from(signupExtraInfo));

		return ResponseEntity.ok(
			Response.success(ResponseCode.SUCCESS_INSERT)
		);
	}

}