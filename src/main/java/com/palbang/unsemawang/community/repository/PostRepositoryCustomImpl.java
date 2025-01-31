package com.palbang.unsemawang.community.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostProjectionDto;
import com.palbang.unsemawang.community.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public LongCursorResponse<PostProjectionDto> findMyPostsByCursor(String memberId, CursorRequest<Long> cursorRequest,
		CommunityCategory category, Sortingtype sortType) {
		QPost post = QPost.post;

		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(post.member.id.eq(memberId));
		whereClause.and(post.isDeleted.eq(false));

		// 특정 카테고리 필터링 (null이면 전체 조회)
		if (category != null) {
			whereClause.and(post.communityCategory.eq(category));
		}

		// 정렬 기준 적용
		OrderSpecifier<?> orderSpecifier;
		if (sortType == Sortingtype.MOST_VIEWED) {
			orderSpecifier = post.viewCount.desc();
		} else { // 기본값은 최신순 정렬
			orderSpecifier = post.registeredAt.desc();
		}

		// 게시글 조회(QueryDSL Projection 사용, 생성자 방식)
		List<PostProjectionDto> posts = queryFactory
			.select(Projections.constructor(PostProjectionDto.class,
				post.id,
				post.title,
				post.content.substring(0, 100), // content 필드에서 100자만 가져오기
				post.viewCount,
				post.likeCount,
				post.commentCount,
				post.member.nickname,
				post.communityCategory,
				post.registeredAt,
				post.updatedAt
			))
			.from(post)
			.where(
				whereClause,
				cursorRequest.key() == null ? null : post.id.lt(cursorRequest.key()) // 커서 기반 페이징 적용
			)
			.orderBy(orderSpecifier, post.id.desc()) // 최신순 또는 조회순 정렬
			.limit(cursorRequest.size() + 1) // 한 개 더 가져와서 다음 커서 판단
			.fetch();

		// 다음 커서 계산
		boolean hasNext = posts.size() > cursorRequest.size();
		if (hasNext) {
			posts.remove(posts.size() - 1);
		}

		Long nextCursorKey = hasNext ? posts.get(posts.size() - 1).getId() : null;

		return LongCursorResponse.of(cursorRequest.next(nextCursorKey), posts);
	}
}
