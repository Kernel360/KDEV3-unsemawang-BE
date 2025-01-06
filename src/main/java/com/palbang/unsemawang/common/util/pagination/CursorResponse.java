package com.palbang.unsemawang.common.util.pagination;

import java.util.List;

/**
 * 커서 기반 페이지 응답
 * @param nextCursorRequest 다음 요청
 * @param data 페이지에 담을 contents
 * @param <T> 응답할 데이터 타입
 */
public record CursorResponse<T>(
	CursorRequest<T> nextCursorRequest,
	List<T> data
) {
}
