package com.palbang.unsemawang.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GeneralException.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(GeneralException ex) {

		log.error("GeneralException occurred: {}", ex.getMessage(), ex);
		ErrorResponse response = ErrorResponse.of(ex.getErrorCode());

		return ResponseEntity
			.status(ex.getErrorCode().getHttpStatus())
			.body(response);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {

		log.error("NullPointerException occurred: {}", ex.getMessage(), ex);
		ErrorResponse response = ErrorResponse.of(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

		log.error("IllegalArgumentException occurred: {}", ex.getMessage(), ex);
		ErrorResponse response = ErrorResponse.of(ResponseCode.DEFAULT_BAD_REQUEST);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {

		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
		ErrorResponse response = ErrorResponse.of(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

		log.error("Validation error: {}", ex.getMessage(), ex);

		// ErrorResponse response = ErrorResponse.of(ResponseCode.DEFAULT_BAD_REQUEST);

		String errorMessage = ex.getBindingResult()
			.getFieldErrors().stream()
			.map(fieldError -> fieldError.getDefaultMessage())
			.collect(Collectors.joining(", "));

		ErrorResponse response = ErrorResponse.of(ResponseCode.DEFAULT_BAD_REQUEST, errorMessage);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}
}
