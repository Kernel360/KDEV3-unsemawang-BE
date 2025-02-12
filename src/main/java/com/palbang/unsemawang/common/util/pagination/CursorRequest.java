package com.palbang.unsemawang.common.util.pagination;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 커서 기반 페이지 요청
 * @param cursorKey unique, sortable cursorKey를 사용해주세요
 * @param size 한번에 요청할 데이터 개수, 디폴트 사이즈 10
 * @param <T> 페이지에 담을 데이터 객체
 */
public record CursorRequest<T>(
	@Schema(description = "커서 키 (첫 요청 시 null 가능)", required = true, nullable = true)
	T cursorKey, @Schema(required = true) Integer size) {

	public CursorRequest(T cursorKey, Integer size) {
		this.cursorKey = cursorKey;
		this.size = defaultSize(size);
	}

	private Integer defaultSize(Integer size) {
		return (size == null || size <= 0) ? 10 : size;
	}

	public CursorRequest<T> next(T cursorKey) {
		return new CursorRequest<>(cursorKey, size);
	}
}
