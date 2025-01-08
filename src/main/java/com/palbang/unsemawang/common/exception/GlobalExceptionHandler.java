package com.palbang.unsemawang.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GeneralException.class)
	public ResponseEntity<Response> handleGeneralException(GeneralException ex) {

		log.error("GeneralException occurred: {}", ex.getMessage(), ex);
		Response response = Response.error(ex.getErrorCode(), ex);

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(response);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Response> handleNullPointerException(NullPointerException ex) {

		log.error("NullPointerException occurred: {}", ex.getMessage(), ex);
		Response response = Response.error(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {

		log.error("IllegalArgumentException occurred: {}", ex.getMessage(), ex);
		Response response = Response.error(ResponseCode.DEFAULT_BAD_REQUEST);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleUnexpectedException(Exception ex) {

		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
		Response response = Response.error(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}
}
