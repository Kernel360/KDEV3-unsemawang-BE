package com.palbang.unsemawang.community.repository;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostProjectionDto;

public interface PostRepositoryCustom {
	LongCursorResponse<PostProjectionDto> findMyPostsByCursor(String memberId, CursorRequest<Long> cursorRequest,
		CommunityCategory category, Sortingtype sortType);
}
