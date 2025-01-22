package com.palbang.unsemawang.community.repository;

import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.entity.Comment;

public interface CommentRepositoryCustom {
	LongCursorResponse<Comment> findCommentsByPostIdAndCursor(Long postId, Long cursorKey, int size);
}
