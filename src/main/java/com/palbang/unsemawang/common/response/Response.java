package com.palbang.unsemawang.common.response;

import com.palbang.unsemawang.common.constants.ResponseCode;

/**
 * 공통 커스텀 응답
 * @param success 성공 여부 true or false
 * @param code 커스텀 응답 코드
 * @param message 응답 메세지
 * @param data 응답 데이터 (데이터가 없을 경우 null)
 */
public record Response<T>(String code, String message) {

	// 실패
	public static <T> Response<T> error(ResponseCode responseCode) {
		return new Response<>(responseCode.getCode(), responseCode.getMessage());
	}

	public static <T> Response<T> error(ResponseCode responseCode, String customMessage) {
		return new Response<>(responseCode.getCode(), responseCode.getMessage(customMessage));
	}
}
