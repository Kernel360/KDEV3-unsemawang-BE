package com.palbang.unsemawang.oauth2.service;

import static com.palbang.unsemawang.member.constant.MemberRole.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.util.RandomKeyGenerator;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;
import com.palbang.unsemawang.oauth2.dto.GoogleResponse;
import com.palbang.unsemawang.oauth2.dto.KakaoResponse;
import com.palbang.unsemawang.oauth2.dto.OAuth2Response;
import com.palbang.unsemawang.oauth2.dto.UserDto;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
	private final MemberRepository memberRepository;

	public CustomOAuth2UserService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		logger.debug("loadUser 호출");
		OAuth2User oAuth2User = super.loadUser(userRequest);
		logger.debug("oAuth2User: {}", oAuth2User);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		String oauthID = oAuth2Response.getProviderId();

		Member existData = memberRepository.findByOauthId(oauthID).orElse(null);
		if (existData == null) {
			//신규회원이면 Member 객체에 정보 DB에 저장
			Member member = Member.builder()
				.id(RandomKeyGenerator.createUUID())
				.oauthId(oAuth2Response.getProviderId())
				.oauthProvider(oAuth2Response.getProvider())
				.email(oAuth2Response.getEmail())
				.role(GENERAL)
				.build();

			//OAuth 얻어온 정보 회원정보 저장
			memberRepository.save(member);

			UserDto userDto = new UserDto();
			userDto.setId(member.getId());
			userDto.setEmail(oAuth2Response.getEmail());
			userDto.setRole("" + GENERAL);
			userDto.setIsJoin(member.getIsJoin());
			return new CustomOAuth2User(userDto);

		} else {
			logger.debug("이미 회원");

			memberRepository.save(existData);

			UserDto userDto = new UserDto();
			userDto.setId(existData.getId());
			userDto.setEmail(oAuth2Response.getEmail());
			userDto.setRole("" + existData.getRole());
			userDto.setIsJoin(existData.getIsJoin());

			return new CustomOAuth2User(userDto);
		}

	}
}