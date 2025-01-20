package com.palbang.unsemawang.common.util.pagination;

import java.util.List;

public record LongCursorResponse<T>(
	CursorRequest<Long> nextCursorRequest, // 커서는 항상 Long 타입
	List<T> data                           // 실제 데이터는 T 타입
) {
	public static <T> LongCursorResponse<T> of(CursorRequest<Long> nextCursorRequest, List<T> data) {
		return new LongCursorResponse<>(nextCursorRequest, data);
	}

	public static <T> LongCursorResponse<T> empty(CursorRequest<Long> nextCursorRequest) {
		return new LongCursorResponse<>(nextCursorRequest, List.of());
	}
}