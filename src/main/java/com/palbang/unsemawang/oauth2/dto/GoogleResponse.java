package com.palbang.unsemawang.oauth2.dto;

import com.palbang.unsemawang.member.constant.OauthProvider;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String,Object> attribute;

    public GoogleResponse(Map<String,Object> attribute) {
        this.attribute = attribute;
    }
    @Override
    public OauthProvider getProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
