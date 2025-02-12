package com.palbang.unsemawang.community.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostProjectionDto;
import com.palbang.unsemawang.community.entity.Post;

public interface PostRepositoryCustom {
	LongCursorResponse<PostProjectionDto> findMyPostsByCursor(String memberId, CursorRequest<Long> cursorRequest,
		CommunityCategory category, Sortingtype sortType);

	List<Post> findLatestPostsByCategory(CommunityCategory category, Long cursorKey, LocalDateTime cursorRegisteredAt,
		int size);

	List<Post> findMostViewedPostsByCategory(CommunityCategory category, Long cursorKey, int size);

	List<Post> findPopularPosts(Long cursorKey, LocalDateTime thirtyDaysAgo, int size);

	List<Post> searchPosts(String keyword, String searchType, Long cursorKey, int size);

}
