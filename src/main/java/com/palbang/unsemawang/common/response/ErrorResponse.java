package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

/**
 * 커스텀 에러 응답
 * new 보다 of 메서드 사용 지향해주세요
 * @param responseCode 커스텀 에러 코드
 * @param message 커스텀 에러 메세지
 */
public record ErrorResponse(String responseCode, String message) {

	public static ErrorResponse of(ResponseCode responseCode) {
		return new ErrorResponse(responseCode.getCode(), responseCode.getMessage());
	}

	public static ErrorResponse of(ResponseCode responseCode, String customMessage) {
		return new ErrorResponse(responseCode.getCode(), customMessage);
	}

}
