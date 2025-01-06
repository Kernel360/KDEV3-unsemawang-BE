package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

/**
 * 기본 응답
 * @param success 성공 여부 true or false
 * @param code 커스텀 응답 코드
 * @param message 응답 메세지
 */
public record BaseResponse(boolean success, String code, String message) {

	public static BaseResponse of(boolean success, ResponseCode responseCode) {
		return new BaseResponse(success, responseCode.getCode(), responseCode.getMessage());
	}

	public static BaseResponse of(boolean success, ResponseCode responseCode, Exception exception) {
		return new BaseResponse(success, responseCode.getCode(), responseCode.getMessage(exception));
	}

	public static BaseResponse of(boolean success, ResponseCode responseCode, String customMessage) {
		return new BaseResponse(success, responseCode.getCode(), responseCode.getMessage(customMessage));
	}
}
