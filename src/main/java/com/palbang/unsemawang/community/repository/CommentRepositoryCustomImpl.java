package com.palbang.unsemawang.community.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public LongCursorResponse<Comment> findCommentsByPostIdAndCursor(Long postId, Long cursorKey, int size) {
		QComment comment = QComment.comment;

		// 댓글 조회
		List<Comment> comments = queryFactory
			.selectFrom(comment)
			.distinct()
			.leftJoin(comment.childComments).fetchJoin() // 대댓글 포함
			.where(
				comment.post.id.eq(postId),               // 게시글 ID 조건
				comment.parentComment.isNull(),           // 부모 댓글만 가져옴
				cursorKey == null ? null : comment.id.lt(cursorKey) // WHERE id < {cursorKey} 로 변환
			)
			.orderBy(comment.id.desc()) // 최신순 정렬
			.limit(size + 1) // 요청한 size보다 1개 더 가져옴
			.fetch();

		// 다음 커서 값 계산
		// comments.size()가 요청한 `size`보다 크면 더 많은 데이터가 남아 있다는 뜻 => 다음 커서 키 계산 필요
		boolean hasNext = comments.size() > size;
		if (hasNext) {
			comments.remove(comments.size() - 1); // 초과 데이터 제거
		}

		Long nextCursorKey = hasNext ? comments.get(comments.size() - 1).getId() : null;

		// LongCursorResponse 생성
		return LongCursorResponse.of(
			new CursorRequest<>(nextCursorKey, size), // 다음 커서 요청
			comments
		);
	}
}