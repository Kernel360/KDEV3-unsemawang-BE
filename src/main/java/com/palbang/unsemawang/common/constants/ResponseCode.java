package com.palbang.unsemawang.common.constants;

import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode implements Codable {

	// 성공 - 0000 고정
	SUCCESS_REQUEST("0000", HttpStatus.OK, "데이터 처리 성공"),
	SUCCESS_SEARCH("0000", HttpStatus.OK, "데이터 조회 성공"),
	SUCCESS_INSERT("0000", HttpStatus.CREATED, "데이터 입력 성공"),
	SUCCESS_UPDATE("0000", HttpStatus.OK, "데이터 수정 성공"),
	SUCCESS_DELETE("0000", HttpStatus.NO_CONTENT, "데이터 삭제 성공"),

	DEFAULT_OK("0000", HttpStatus.OK, "Ok"),

	// JWT 오류
	JWT_EXPIRED("3001", HttpStatus.UNAUTHORIZED, "만료된 JWT"),
	JWT_INVALID("3002", HttpStatus.BAD_REQUEST, "유효하지 않은 JWT"),

	DEFAULT_BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 요청을 확인해주세요."),
	RESOURCE_NOT_FOUND("4040", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	// 서버 오류 (500번대 서버 오류, 500 Internal Server Error)
	DEFAULT_INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생"),

	// CRUD 오류 (511x, 수정/삭제/요청 실패)
	ERROR_MODIFY("5111", HttpStatus.BAD_REQUEST, "수정 실패"),
	ERROR_DELETE("5112", HttpStatus.BAD_REQUEST, "삭제 실패"),
	ERROR_REQUEST("5113", HttpStatus.BAD_REQUEST, "데이터 처리 실패"),
	ERROR_SEARCH("5114", HttpStatus.BAD_REQUEST, "데이터 조회 실패"),
	ERROR_INSERT("5115", HttpStatus.BAD_REQUEST, "데이터 입력 실패"),

	//	유효성 검사 오류 (6xxx)
	// 유효성 검사 오류 (인증: 61xx)
	IS_NOT_SUBSCRIBER("6101", HttpStatus.BAD_REQUEST, "구독자가 아님"),
	EMPTY_TOKEN("6102", HttpStatus.BAD_REQUEST, "TOKEN 데이터 누락"),
	EMPTY_UNIQUE_NO("6103", HttpStatus.BAD_REQUEST, "고유번호 데이터 누락"),
	ILLEGAL_HEADER("6104", HttpStatus.UNAUTHORIZED, "TOKEN 과 고유번호 미일치(인증 실패)"),
	NOT_EXIST_TOKEN("6105", HttpStatus.UNAUTHORIZED, "유효하지 않은 TOKEN"),
	NOT_EXIST_UNIQUE_NO("6106", HttpStatus.UNAUTHORIZED, "유효하지 않은 고유번호"),
	NOT_EXIST_GROUP("6111", HttpStatus.BAD_REQUEST, "유효하지 않은 요청 group"),
	NOT_EXIST_ID("6112", HttpStatus.BAD_REQUEST, "유효하지 않은 요청 id"),
	NOT_EXIST_TYPE("6113", HttpStatus.BAD_REQUEST, "유효하지 않은 요청 type"),

	//  유효성 검사 오류 (형식: 62xx)
	NOT_LITERAL("6211", HttpStatus.BAD_REQUEST, "문자열 형식이 아님"),
	NOT_NUMERIC("6212", HttpStatus.BAD_REQUEST, "0 이상의 숫자(양수)형식이 아님"),
	NOT_SUPPORT_TYPE("6213", HttpStatus.BAD_REQUEST, "지원하지 않는 형식의 Parameter"),
	NOT_SUPPORT_DATE_FORMAT("6214", HttpStatus.BAD_REQUEST, "지원하지 않는 날짜 형식"),
	NOT_SUPPORT_TIME_FORMAT("6215", HttpStatus.BAD_REQUEST, "지원하지 않는 시간 형식"),
	NOT_SUPPORT_PHONE_FORMAT("6216", HttpStatus.BAD_REQUEST, "지원하지 않는 연락처 형식"),
	MIN_VALUE("6221", HttpStatus.BAD_REQUEST, "최소 값 기준치 미달"),
	MAX_VALUE("6222", HttpStatus.BAD_REQUEST, "최대 값 기준치 초과"),
	DUPLICATED_VALUE("6223", HttpStatus.CONFLICT, "이미 사용 중인 닉네임 입니다."),

	// 유효성 검사 오류 (값: 63xx)
	EMPTY_PARAM_BLANK_OR_NULL("6300", HttpStatus.BAD_REQUEST, "Request Parameter 빈 값, NULL 또는 공백"),
	EMPTY_PARAM_01("6301", HttpStatus.BAD_REQUEST, "01번 Parameter 데이터 누락"),
	EMPTY_PARAM_02("6302", HttpStatus.BAD_REQUEST, "02번 Parameter 데이터 누락"),
	EMPTY_PARAM_03("6303", HttpStatus.BAD_REQUEST, "03번 Parameter 데이터 누락"),
	EMPTY_PARAM_04("6304", HttpStatus.BAD_REQUEST, "04번 Parameter 데이터 누락"),
	EMPTY_PARAM_05("6305", HttpStatus.BAD_REQUEST, "05번 Parameter 데이터 누락"),
	EMPTY_PARAM_06("6306", HttpStatus.BAD_REQUEST, "06번 Parameter 데이터 누락"),
	EMPTY_PARAM_07("6307", HttpStatus.BAD_REQUEST, "07번 Parameter 데이터 누락"),
	EMPTY_PARAM_08("6308", HttpStatus.BAD_REQUEST, "08번 Parameter 데이터 누락"),
	EMPTY_PARAM_09("6309", HttpStatus.BAD_REQUEST, "09번 Parameter 데이터 누락"),

	// 강제 에러
	TEST_ERROR("9999", HttpStatus.INTERNAL_SERVER_ERROR, "강제 발생 ERROR");

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

	public String getMessage(Throwable throwable) {
		return this.getMessage(this.getMessage() + " - " + throwable.getMessage());
	}

	public String getMessage(String message) {
		return Optional.ofNullable(message)
			.filter(Predicate.not(String::isBlank))
			.orElse(this.getMessage());
	}

	@Override
	public String toString() {
		return String.format("%s (%s) %s", this.name(), this.getCode(), this.getMessage());
	}

	@Override
	public String getKey() {
		return this.code;
	}

	@Override
	public String getValue() {
		return this.message;
	}
}
