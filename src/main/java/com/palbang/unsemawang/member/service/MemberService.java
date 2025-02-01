package com.palbang.unsemawang.member.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.jwt.JWTUtil;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.dto.MemberProfileDto;
import com.palbang.unsemawang.member.dto.SignupExtraInfoRequest;
import com.palbang.unsemawang.member.dto.request.UpdateMemberRequest;
import com.palbang.unsemawang.member.dto.response.UpdateMemberResponse;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final JWTUtil jwtUtil;

	// nickname 중복 여부 확인
	public void duplicateNicknameCheck(String nickname) {
		if (memberRepository.findByNickname(nickname).isPresent()) {
			throw new GeneralException(ResponseCode.DUPLICATED_VALUE);
		}
	}

	//회원 추가 정보 입력
	@Transactional(rollbackFor = Exception.class)
	public void signupExtraInfo(SignupExtraInfoRequest signupExtraInfo) {
		// 회원 정보 조회
		Member member = memberRepository.findById(signupExtraInfo.getId())
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		// 닉네임 중복체크
		duplicateNicknameCheck(signupExtraInfo.getNickname());

		// 추가 정보 업데이트
		member.updateExtraInfo(signupExtraInfo);

		//회원가입 추가정보 입력 여부 값 변경
		member = member.toBuilder()
			.isJoin(true)
			.role(MemberRole.GENERAL)
			.build();

		memberRepository.save(member);

	}

	public MemberProfileDto getMemberProfile(String id) {

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		MemberProfileDto memberProfileDto = MemberProfileDto.builder()
			.role(member.getRole())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.isJoin(member.getIsJoin())
			.profileUrl(member.getProfileUrl())
			.point(member.getPoint())
			.oauthProvider(member.getOauthProvider())
			.detailBio(member.getDetailBio())
			.build();

		return memberProfileDto;
	}

	//회원 개인정보수정
	public UpdateMemberResponse updateMemberProfile(String id, UpdateMemberRequest updateMemberRequest) {
		// 회원 검증
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "회원을 찾을 수 없습니다"));

		String nickname = updateMemberRequest.getNickname();
		String detailBio = updateMemberRequest.getDetailBio();

		if(!member.getNickname().equals(nickname)) {
			//닉네임 중복 체크
			duplicateNicknameCheck(nickname);
		}

		Member updateMember = member.toBuilder()
			.nickname(nickname)
			.detailBio(detailBio)
			.build();

		memberRepository.save(updateMember);

		return new UpdateMemberResponse(updateMember.getNickname(), updateMember.getDetailBio());
	}

	//jwt 발급후 쿠키에 넣어서 전달
	public HttpHeaders addJwtToCookie(@AuthenticationPrincipal CustomOAuth2User auth){
		HttpHeaders headers = new HttpHeaders();
		String token = jwtUtil.createJTwt(auth.getId(), auth.getEmail(),"GENERAL",60 * 60 * 1000L * 24 * 3);
		// 쿠키 추가
		ResponseCookie responseCookie = createResponseCookie("Authorization", token, 60 * 60 * 24 * 3);

		headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

		return headers;
	}

	private ResponseCookie createResponseCookie(String key, String value, int maxAge) {
		// ResponseCookie 생성 시 SameSite=None 설정
		return ResponseCookie.from(key, value)
			.path("/")
			.domain("unsemawang.com") // 도메인 지정 (필요한 경우 설정)
			.sameSite("None") // SameSite=None 설정
			.secure(true)     // HTTPS에서만 전송
			.httpOnly(true)   // JavaScript 접근 불가
			.maxAge(maxAge)   // 만료 시간 설정
			.build();
	}
	private ResponseCookie createJsessionCookie(String key, String value, int maxAge){
		return ResponseCookie.from(key, value)
			.path("/")
			.domain("dev.unsemawang.com") // 도메인 지정 (필요한 경우 설정)
			.httpOnly(true)   // JavaScript 접근 불가
			.maxAge(maxAge)   // 만료 시간 설정
			.build();
	}

	//쿠키를 지워주는 로그아웃
	public HttpHeaders invalidateJwtCookie(@AuthenticationPrincipal CustomOAuth2User auth){
		HttpHeaders headers = new HttpHeaders();
		String token = jwtUtil.createJTwt(auth.getId(), auth.getEmail(),"GENERAL",0L);
		// 쿠키 추가
		ResponseCookie responseCookie = createResponseCookie("Authorization", token, 0);
		ResponseCookie jsessionCookie = createJsessionCookie("JSESSIONID", "", 0);
		headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
		headers.add(HttpHeaders.SET_COOKIE, jsessionCookie.toString());
		return headers;
	}
}
