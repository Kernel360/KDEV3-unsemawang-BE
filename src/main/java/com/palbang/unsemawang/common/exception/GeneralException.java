package com.palbang.unsemawang.common.exception;

import com.palbang.unsemawang.common.constants.ResponseCode;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

	private final ResponseCode errorCode;

	public GeneralException() {
		super(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR.getMessage());
		this.errorCode = ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR;
	}

	public GeneralException(ResponseCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public GeneralException(ResponseCode errorCode, String message) {
		super(errorCode.getMessage(message));
		this.errorCode = errorCode;
	}

	public GeneralException(ResponseCode errorCode, Throwable cause) {
		super(errorCode.getMessage(cause), cause);
		this.errorCode = errorCode;
	}

	public GeneralException(ResponseCode errorCode, String message, Throwable cause) {
		super(errorCode.getMessage(message), cause);
		this.errorCode = errorCode;
	}

}
