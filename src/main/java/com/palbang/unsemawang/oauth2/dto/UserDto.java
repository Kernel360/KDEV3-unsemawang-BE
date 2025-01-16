package com.palbang.unsemawang.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	private String id;
	private String role;
	private String email;
}