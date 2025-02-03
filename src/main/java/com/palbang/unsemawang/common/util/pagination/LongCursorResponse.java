package com.palbang.unsemawang.common.util.pagination;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record LongCursorResponse<T>(
	@Schema(required = true)
	CursorRequest<Long> nextCursorRequest,// 커서는 항상 Long 타입
	@Schema(required = true)
	Boolean hasNextCursor,
	@Schema(required = true)
	List<T> data                           // 실제 데이터는 T 타입
) {
	public static <T> LongCursorResponse<T> of(CursorRequest<Long> nextCursorRequest, List<T> data) {
		// hasNextCursor는 data 리스트가 비어 있지 않은 경우 true로 설정
		boolean hasNextCursor = (data != null && !data.isEmpty());
		return new LongCursorResponse<>(nextCursorRequest, hasNextCursor, data);
	}

	public static <T> LongCursorResponse<T> empty(CursorRequest<Long> nextCursorRequest) {
		// 빈 응답에는 hasNextCursor를 false로 설정
		return new LongCursorResponse<>(nextCursorRequest, false, List.of());
	}
}