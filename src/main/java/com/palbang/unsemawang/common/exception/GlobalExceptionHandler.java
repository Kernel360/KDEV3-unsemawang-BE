package com.palbang.unsemawang.common.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.ErrorResponse;
import com.palbang.unsemawang.common.util.file.exception.FileDeleteException;
import com.palbang.unsemawang.common.util.file.exception.FileNotFoundException;
import com.palbang.unsemawang.common.util.file.exception.FileSizeExceededException;
import com.palbang.unsemawang.common.util.file.exception.FileUploadException;
import com.palbang.unsemawang.common.util.file.exception.InvalidFileFormatException;

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
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
		log.error("Validation error: {}", ex.getMessage(), ex);

		// 모든 필드 에러 메시지를 리스트로 추출
		List<String> messages = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getDefaultMessage())
			.toList();

		// 커스텀 에러 응답 생성
		Map<String, Object> response = new HashMap<>();
		response.put("message", messages);
		response.put("error", "Bad Request");
		response.put("statusCode", 400);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	// 파일 관련 예외 -------------------------------------------------------------------

	@ExceptionHandler(FileDeleteException.class)
	public ResponseEntity<ErrorResponse> handleFileDeleteException(FileDeleteException ex) {

		log.error("FileDeleteException occurred: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(ResponseCode.ERROR_DELETE);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException ex) {

		log.error("FileNotFoundException occurred: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(ResponseCode.ERROR_SEARCH);

		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(response);
	}

	@ExceptionHandler(FileSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleFileSizeExceededException(FileSizeExceededException ex) {

		log.error("FileSizeExceededException occurred: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(ResponseCode.MAX_VALUE);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException ex) {

		log.error("FileUploadException occurred: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(ResponseCode.ERROR_INSERT);

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(response);
	}

	@ExceptionHandler(InvalidFileFormatException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFileFormatException(InvalidFileFormatException ex) {

		log.error("InvalidFileFormatException occurred: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(ResponseCode.NOT_EXIST_TYPE);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(response);
	}
}
