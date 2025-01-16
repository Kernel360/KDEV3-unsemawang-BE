package com.palbang.unsemawang.oauth2.dto;

import java.util.Map;

import com.palbang.unsemawang.member.constant.OauthProvider;

public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public KakaoResponse(Map<String, Object> attribute) {
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
