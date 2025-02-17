package com.palbang.unsemawang.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chemistry.batch.TotalCalculationService;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.response.ErrorResponse;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.fortune.dto.request.FortuneInfoRegisterRequest;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoRegisterService;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoUpdateService;
import com.palbang.unsemawang.member.dto.MemberProfileDto;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;
import com.palbang.unsemawang.member.dto.request.UpdateMemberRequest;
import com.palbang.unsemawang.member.dto.response.UpdateMemberResponse;
import com.palbang.unsemawang.member.service.MemberService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "회원")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final FortuneUserInfoRegisterService fortuneInfoRegisterService;
	private final FileService fileService;
	private final FortuneUserInfoUpdateService fortuneUserInfoUpdateService;
	private final TotalCalculationService totalCalculationService;

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
			regexp = "^[A-Za-z\\d가-힣]{2,10}$",
			message = "닉네임은 2~10자 이내의 문자(한글, 영어 대소문자, 숫자)여야 합니다."
		)
		String nickname) {

		memberService.duplicateNicknameCheck(nickname);

		return ResponseEntity.ok().build();
	}

	@Operation(
		description = "회원가입 과정 중 추가정보 입력을 위한 api 입니다. 인증 토큰이 담긴 쿠키를 직접 보내셔야 테스트가 가능합니다!",
		summary = "회원가입 - 추가정보 입력"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200", description = "회원가입 성공"
		),
		@ApiResponse(
			responseCode = "409", description = "이미 가입된 회원이여서 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))
		)
	})
	// 회원 가입 추가정보 입력
	@PostMapping("/signup/extra-info")
	public ResponseEntity signupExtraInfo(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody SignupExtraInfoRequest signupExtraInfo
	) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		// 인증 객체로부터 회원 id 값 가져와 세팅
		signupExtraInfo.updateMemberId(auth.getId());

		// 추가 정보 업데이트
		memberService.signupExtraInfo(signupExtraInfo);

		// 사주 정보 등록
		fortuneInfoRegisterService.registerFortuneInfo(FortuneInfoRegisterRequest.from(signupExtraInfo));

		// HttpHeaders 객체 JWT 재발급 후 쿠키에 넘겨줌
		HttpHeaders headers = memberService.addJwtToCookie(auth);

		// 궁합 점수 계산 - 비동기
		totalCalculationService.calculateAndSaveChemistryScoresForNewMember(auth.getId());

		return ResponseEntity.ok()
			.headers(headers)
			.build();
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

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		//회원정보에 해당하는 프로필 조회
		MemberProfileDto memberProfile = memberService.getMemberProfile(auth.getId());

		//회원 프로필URL 조회
		String url = fileService.getProfileImgUrl(auth.getId());
		memberProfile.setProfileUrl(url);

		return ResponseEntity.ok(memberProfile);
	}

	@Operation(
		summary = "회원 개인정보 수정",
		description = "회원 개인정보를 수정합니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@PutMapping("/profile/me")
	public ResponseEntity<Response> updateProfile(
		@AuthenticationPrincipal CustomOAuth2User auth,
		@Valid @RequestBody UpdateMemberRequest updateMemberRequest) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		String id = auth.getId();

		//회원정보수정
		UpdateMemberResponse updateMemberResponse = memberService.updateMemberProfile(id, updateMemberRequest);

		//본인사주 닉네임 수정
		fortuneUserInfoUpdateService.updateFortuneUserNickname(id, updateMemberResponse.getNickname());

		return ResponseEntity.ok(
			Response.success(ResponseCode.SUCCESS_UPDATE, updateMemberResponse, "회원정보가 정상적으로 수정되었습니다.")
		);
	}

	@PostMapping("/logout")
	public ResponseEntity<Response> logout(
		@AuthenticationPrincipal CustomOAuth2User auth) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		//쿠키의 유효기간 변경해주는 서비스호출
		HttpHeaders headers = memberService.invalidateJwtCookie(auth);
		return ResponseEntity.ok()
			.headers(headers)
			.body(Response.success(ResponseCode.SUCCESS_REQUEST));
	}
}