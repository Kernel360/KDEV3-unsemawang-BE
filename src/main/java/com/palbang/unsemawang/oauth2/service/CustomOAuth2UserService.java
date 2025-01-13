package com.palbang.unsemawang.oauth2.service;

import com.palbang.unsemawang.oauth2.dto.*;
import com.palbang.unsemawang.common.util.RandomKeyGenerator;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.palbang.unsemawang.member.constant.MemberRole.GENERAL;

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

        if(registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        String oauthID = oAuth2Response.getProviderId();

        Member existData = memberRepository.findByOauthId(oauthID).orElse(null);
        if(existData == null){
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

            UserDTO userDTO = new UserDTO();
            userDTO.setId(member.getId());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setRole(""+GENERAL);
            return new CustomOAuth2User(userDTO);

        }else{
            logger.debug("이미 회원");

            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(existData.getId());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setRole(""+existData.getRole());

            return new CustomOAuth2User(userDTO);
        }

    }
}