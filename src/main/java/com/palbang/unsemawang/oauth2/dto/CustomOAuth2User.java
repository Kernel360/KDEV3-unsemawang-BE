package com.palbang.unsemawang.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
	private final UserDto userDto;

	public CustomOAuth2User(final UserDto userDto) {
		this.userDto = userDto;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userDto.getRole();
			}
		});
		return collection;
	}

	@Override
	public String getName() {
		return userDto.getId();
	}

	public String getId() {
		return userDto.getId();
	}

	public String getEmail() {
		return userDto.getEmail();
	}

}
