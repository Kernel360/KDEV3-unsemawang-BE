package com.palbang.unsemawang.fortune.dto.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteResponseDto {
	private final ResponseCode responseCode;
	private final String message;
}