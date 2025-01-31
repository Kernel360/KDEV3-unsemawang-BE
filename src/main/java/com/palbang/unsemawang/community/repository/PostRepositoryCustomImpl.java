package com.palbang.unsemawang.community.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.common.util.pagination.CursorRequest;
import com.palbang.unsemawang.common.util.pagination.LongCursorResponse;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.constant.Sortingtype;
import com.palbang.unsemawang.community.dto.response.PostListResponse;
import com.palbang.unsemawang.community.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final FileService fileService;

	@Override
	public LongCursorResponse<PostListResponse> findMyPostsByCursor(String memberId, CursorRequest<Long> cursorRequest,
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

		// 게시글 조회(fileService 호출 없이 QueryDSL로만 데이터 가져옴)
		List<PostListResponse> posts = queryFactory
			.select(Projections.fields(PostListResponse.class,
				post.id.as("id"),
				post.title.as("title"),
				Expressions.stringTemplate("substring({0}, 1, {1})", post.content, 100).as("snippet"),
				// String으로 변환 명확히
				post.viewCount.as("viewCount"),
				post.likeCount.as("likeCount"),
				post.commentCount.as("commentCount"),
				post.member.nickname.as("nickname"),
				post.communityCategory.as("communityCategory"),
				post.registeredAt.as("registeredAt"),
				post.updatedAt.as("updatedAt")
			))
			.from(post)
			.where(whereClause)
			.orderBy(orderSpecifier, post.id.desc()) // 최신순 또는 조회순 정렬
			.limit(cursorRequest.size() + 1) // 한 개 더 가져와서 다음 커서 판단
			.fetch();

		// ✅ QueryDSL에서 파일 URL을 못 가져오니까, 여기서 fileService 호출
		List<PostListResponse> data = posts.stream()
			.map(postData -> PostListResponse.builder()
				.cursorId(null) // ✅ QueryDSL에서 null로 조회했으므로 여기서도 null 유지
				.id(postData.getId())
				.title(postData.getTitle())
				.snippet(postData.getSnippet())
				.viewCount(postData.getViewCount())
				.likeCount(postData.getLikeCount())
				.commentCount(postData.getCommentCount())
				.nickname(postData.getNickname())
				.profileImageUrl(postData.getProfileImageUrl()) // QueryDSL에서 가져온 값 그대로 사용
				.communityCategory(postData.getCommunityCategory()) // QueryDSL에서 가져온 값 그대로 사용
				.registeredAt(postData.getRegisteredAt())
				.updatedAt(postData.getUpdatedAt())
				.imageUrl(fileService.getPostThumbnailImgUrl(postData.getId())) // ✅ 파일 서비스에서 이미지 URL 가져오기
				.build()
			).toList();

		// 다음 커서 계산
		boolean hasNext = posts.size() > cursorRequest.size();
		if (hasNext) {
			posts.remove(posts.size() - 1);
		}

		Long nextCursorKey = hasNext ? posts.get(posts.size() - 1).getId() : null;

		return LongCursorResponse.of(cursorRequest.next(nextCursorKey), data);
	}
}
