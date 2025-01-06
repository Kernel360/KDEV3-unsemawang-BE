package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

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
