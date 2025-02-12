package com.palbang.unsemawang.community.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.dto.response.MyCommentsReadResponse;
import com.palbang.unsemawang.community.entity.Comment;
import com.palbang.unsemawang.community.entity.QComment;
import com.palbang.unsemawang.community.entity.QPost;
import com.querydsl.core.types.Projections;
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
			.where(
				comment.post.id.eq(postId),               // 게시글 ID 조건
				comment.parentComment.isNull(),           // 부모 댓글만 조회
				comment.isDeleted.eq(false),
				cursorKey == null ? null : comment.id.gt(cursorKey) // 커서 조건
			)
			.orderBy(comment.id.asc())
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
		return LongCursorResponse.of(new CursorRequest<>(nextCursorKey, size), comments);
	}

	@Override
	public List<Comment> findChildCommentsByParentIds(List<Long> parentIds) {
		QComment comment = QComment.comment;

		return queryFactory
			.selectFrom(comment)
			.where(
				comment.parentComment.id.in(parentIds), // 부모 댓글 ID 조건
				comment.isDeleted.eq(false)
			)
			.orderBy(comment.parentComment.id.asc(), comment.id.asc()) // 부모 ID 및 자식 정렬
			.fetch();
	}

	@Override
	public LongCursorResponse<MyCommentsReadResponse> findMyCommentsByCursor(String memberId,
		CursorRequest<Long> cursorRequest) {
		QComment comment = QComment.comment;
		QPost post = QPost.post;

		List<MyCommentsReadResponse> comments = queryFactory
			.select(Projections.constructor(MyCommentsReadResponse.class,
				comment.id,
				post.id,
				post.communityCategory,
				post.title,
				comment.content,
				comment.registeredAt,
				post.isDeleted
			))
			.from(comment)
			.join(comment.post, post) // Post와 함께 가져옴
			.where(
				comment.member.id.eq(memberId),
				comment.isDeleted.eq(false),
				cursorRequest.cursorKey() == null ? null : comment.id.lt(cursorRequest.cursorKey())
			)
			.orderBy(comment.registeredAt.desc(), comment.id.desc())
			.limit(cursorRequest.size() + 1)
			.fetch();

		// 다음 커서 계산
		boolean hasNext = comments.size() > cursorRequest.size();
		if (hasNext) {
			comments.remove(comments.size() - 1); // 초과 데이터 제거
		}

		Long nextCursorKey = hasNext ? comments.get(comments.size() - 1).getCommentId() : null;

		return LongCursorResponse.of(cursorRequest.next(nextCursorKey), comments);
	}
}