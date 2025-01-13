package com.palbang.unsemawang.oauth2.dto;

import com.palbang.unsemawang.member.constant.OauthProvider;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String,Object> attribute;

    public KakaoResponse(Map<String,Object> attribute) {
        this.attribute = attribute;
    }
    @Override
    public OauthProvider getProvider() {
        return OauthProvider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }
}
