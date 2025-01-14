package com.palbang.unsemawang.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostRegisterResponse {

	private String message;

	public static PostRegisterResponse of(String message) {
		return PostRegisterResponse.builder()
			.message(message)
			.build();
	}
}
