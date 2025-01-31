package com.palbang.unsemawang.community.repository;

import java.util.List;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.response.MyCommentsReadResponse;
import com.palbang.unsemawang.community.entity.Comment;

public interface CommentRepositoryCustom {
	LongCursorResponse<Comment> findCommentsByPostIdAndCursor(Long postId, Long cursorKey, int size);

	List<Comment> findChildCommentsByParentIds(List<Long> parentIds);

	LongCursorResponse<MyCommentsReadResponse> findMyCommentsByCursor(String memberId,
		CursorRequest<Long> cursorRequest);
}
