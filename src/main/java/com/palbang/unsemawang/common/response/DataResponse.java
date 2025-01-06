package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

public record DataResponse<T>(BaseResponse baseResponse, T data) {

	public static <T> DataResponse<T> empty() {
		BaseResponse baseResponse = BaseResponse.of(true, ResponseCode.DEFAULT_OK);
		return new DataResponse<>(baseResponse, null);
	}

	public static <T> DataResponse<T> of(ResponseCode responseCode) {
		BaseResponse baseResponse = BaseResponse.of(true, responseCode);
		return new DataResponse<>(baseResponse, null);
	}

	public static <T> DataResponse<T> of(ResponseCode responseCode, T data) {
		BaseResponse baseResponse = BaseResponse.of(true, responseCode);
		return new DataResponse<>(baseResponse, data);
	}

	public static <T> DataResponse<T> of(ResponseCode responseCode, T data, String message) {
		BaseResponse baseResponse = BaseResponse.of(true, responseCode, message);
		return new DataResponse<>(baseResponse, data);
	}
}
