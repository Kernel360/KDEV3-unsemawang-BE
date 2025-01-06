package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

/**
 * 에러 응답
 * @param baseResponse 기본 응답
 */
public record ErrorResponse(BaseResponse baseResponse) {

	public static ErrorResponse of(ResponseCode responseCode) {
		BaseResponse baseResponse = BaseResponse.of(false, responseCode);
		return new ErrorResponse(baseResponse);
	}

	public static ErrorResponse of(ResponseCode responseCode, Exception exception) {
		BaseResponse baseResponse = BaseResponse.of(false, responseCode, exception);
		return new ErrorResponse(baseResponse);
	}

	public static ErrorResponse of(ResponseCode responseCode, String message) {
		BaseResponse baseResponse = BaseResponse.of(false, responseCode, message);
		return new ErrorResponse(baseResponse);
	}
}
