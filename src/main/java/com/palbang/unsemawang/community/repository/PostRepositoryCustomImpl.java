package com.palbang.unsemawang.community.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostProjectionDto;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QPost post = QPost.post;

	@Override
	public LongCursorResponse<PostProjectionDto> findMyPostsByCursor(String memberId, CursorRequest<Long> cursorRequest,
		CommunityCategory category, Sortingtype sortType) {

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

	private BooleanExpression isCursorBefore(Long cursorKey, LocalDateTime cursorRegisteredAt) {
		if (cursorKey == null || cursorRegisteredAt == null) {
			return null;
		}
		return post.registeredAt.lt(cursorRegisteredAt)
			.or(post.registeredAt.eq(cursorRegisteredAt)
				.and(post.id.lt(cursorKey)));
	}

	private BooleanExpression isCursorForMostViewed(Long cursorKey) {
		if (cursorKey == null) {
			return null;
		}

		// 현재 커서 ID 기준으로 viewCount와 ID를 조회
		Integer cursorViewCount = queryFactory
			.select(post.viewCount)
			.from(post)
			.where(post.id.eq(cursorKey))
			.fetchOne();

		if (cursorViewCount == null) {
			return null; // 조회된 커서가 없으면 조건 없음
		}

		// 커서 기반 조건 추가: viewCount가 작거나 같고 viewCount가 동일하면 ID가 작은 게시글만
		return post.viewCount.lt(cursorViewCount)
			.or(post.viewCount.eq(cursorViewCount).and(post.id.lt(cursorKey)));
	}

	private BooleanExpression isCursorForPopular(Long cursorKey) {
		if (cursorKey == null) {
			return null;
		}

		// 현재 커서 ID 기준으로 점수(`popularityScore`)와 ID를 조회
		Integer cursorPopularityScore = queryFactory
			.select(post.viewCount.multiply(7).add(post.commentCount.multiply(3)).intValue())
			.from(post)
			.where(post.id.eq(cursorKey))
			.fetchOne();

		if (cursorPopularityScore == null) {
			return null; // 조회된 커서가 없으면 조건 없음
		}

		// 커서 기반 조건: 점수가 낮거나 같고, 점수가 동일한 경우 ID가 작은 게시글만
		return post.viewCount.multiply(7).add(post.likeCount.multiply(3)).lt(cursorPopularityScore)
			.or(post.viewCount.multiply(7).add(post.likeCount.multiply(3)).eq(cursorPopularityScore)
				.and(post.id.lt(cursorKey)));
	}

	private BooleanExpression isVisible() {
		return post.isVisible.isTrue().and(post.isDeleted.isFalse());
	}

	@Override
	public List<Post> findLatestPostsByCategory(CommunityCategory category, Long cursorKey,
		LocalDateTime cursorRegisteredAt, int size) {
		return queryFactory
			.selectFrom(post)
			.where(
				post.communityCategory.eq(category),
				isVisible(),
				isCursorBefore(cursorKey, cursorRegisteredAt)
			)
			.orderBy(post.registeredAt.desc(), post.id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Post> findMostViewedPostsByCategory(CommunityCategory category, Long cursorKey, int size) {
		return queryFactory
			.selectFrom(post)
			.where(
				post.communityCategory.eq(category),
				isVisible(),
				isCursorForMostViewed(cursorKey) // updated 커서 조건
			)
			.orderBy(post.viewCount.desc(), post.id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Post> findPopularPosts(Long cursorKey, LocalDateTime thirtyDaysAgo, int size) {
		return queryFactory
			.selectFrom(post)
			.where(
				isVisible(), // 게시글 상태 조건
				post.registeredAt.goe(thirtyDaysAgo), // 30일 이내의 데이터만 조회
				isCursorForPopular(cursorKey) // 수정된 커서 조건
			)
			.orderBy(post.viewCount.multiply(7).add(post.commentCount.multiply(3)).desc(), post.id.desc()) // 정렬 조건
			.limit(size) // 페이징
			.fetch();
	}

	@Override
	public List<Post> searchPosts(String keyword, String searchType, Long cursorKey, int size) {
		return queryFactory
			.selectFrom(post)
			.where(
				cursorKey != null ? post.id.lt(cursorKey) : null,
				isVisible(),
				buildSearchCondition(keyword, searchType)
			)
			.orderBy(post.id.desc())
			.limit(size)
			.fetch();
	}

	private BooleanExpression buildSearchCondition(String keyword, String searchType) {
		if ("all".equals(searchType)) {
			return post.title.containsIgnoreCase(keyword)
				.or(post.content.containsIgnoreCase(keyword))
				.or(post.member.nickname.containsIgnoreCase(keyword).and(post.isAnonymous.isFalse()));
		} else if ("title".equals(searchType)) {
			return post.title.containsIgnoreCase(keyword);
		} else if ("content".equals(searchType)) {
			return post.content.containsIgnoreCase(keyword);
		} else if ("writer".equals(searchType)) {
			return post.member.nickname.containsIgnoreCase(keyword)
				.and(post.isAnonymous.isFalse());
		}
		return null;
	}

}
