package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

/**
 * 데이터 포함 응답
 * @param baseResponse 기본 응답
 * @param data 응답 데이터
 * @param <T> 응답할 데이터 타입
 */
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
