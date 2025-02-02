package com.palbang.unsemawang.common.util.pagination;

import org.springframework.lang.NonNull;

/**
 * 커서 기반 페이지 요청
 * @param key unique, sortable key를 사용해주세요
 * @param size 한번에 요청할 데이터 개수, 디폴트 사이즈 10
 * @param <T> 페이지에 담을 데이터 객체
 */
public record CursorRequest<T>(T key, @NonNull Integer size) {

	public CursorRequest(T key, Integer size) {
		this.key = key;
		this.size = defaultSize(size);
	}

	private Integer defaultSize(Integer size) {
		return (size == null || size <= 0) ? 10 : size;
	}

	public CursorRequest<T> next(T key) {
		return new CursorRequest<>(key, size);
	}
}
