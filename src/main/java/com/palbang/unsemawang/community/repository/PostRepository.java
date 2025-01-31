package com.palbang.unsemawang.community.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findByIdAndMember(Long id, Member member);

	@Query("SELECT p FROM Post p WHERE p.id = :postId AND p.member = :member AND p.isDeleted = false")
	Optional<Post> findByMemberIsNotDeleted(Long postId, Member member);

	// 조회수 증가
	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
	void incrementViewCount(@Param("postId") Long postId);

	// 카테고리에 따른 최신 게시글 가져오기
	@Query("""
	SELECT p FROM Post p
	WHERE p.communityCategory = :category
	AND p.isVisible = true
	AND p.isDeleted = false
	AND (:cursorId IS NULL OR 
         p.registeredAt < :cursorRegisteredAt OR 
         (p.registeredAt = :cursorRegisteredAt AND p.id < :cursorId))
	ORDER BY p.registeredAt DESC, p.id DESC
	LIMIT :size
	""")
	List<Post> findLatestPostsByCategory(
			@Param("category") CommunityCategory category,
			@Param("cursorId") Long cursorId,
			@Param("cursorRegisteredAt") LocalDateTime cursorRegisteredAt,
			@Param("size") int size
	);

	@Query("SELECT p.registeredAt FROM Post p WHERE p.id = :id")
	LocalDateTime findRegisteredAtById(@Param("id") Long id);

	// 카테고리별 조회수 순 게시글 가져오기
	@Query("""
		    SELECT p FROM Post p
		    WHERE (p.id < :cursorId OR :cursorId IS NULL)
		    AND p.communityCategory = :category
		    AND p.isVisible = true
		    AND p.isDeleted = false
		    ORDER BY p.viewCount DESC, p.id DESC
		    LIMIT :size
		""")
	List<Post> findMostViewedPostsByCategory(
		@Param("category") CommunityCategory category,
		@Param("cursorId") Long cursorId,
		@Param("size") int size
	);

	// 인기 게시글 가져오기(최근 30일)
	@Query("""
		    SELECT p FROM Post p
		    WHERE p.isVisible = true
		    AND p.isDeleted = false
		    AND (p.id < :cursorId OR :cursorId IS NULL)
		    AND p.registeredAt >= :thirtyDaysAgo
		    ORDER BY (p.viewCount * 7) + (p.likeCount * 3) DESC, p.id DESC
		    LIMIT :size
		""")
	List<Post> findPopularPosts(
		@Param("cursorId") Long cursorId,
		@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo,
		@Param("size") int size
	);

	// 게시글 검색
	@Query("""
    SELECT p FROM Post p
    WHERE (p.id < :cursorId OR :cursorId IS NULL)
    AND (
        (:searchType = 'all' AND (
            LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(p.member.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ))
        OR (:searchType = 'title' AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:searchType = 'content' AND LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:searchType = 'writer' AND LOWER(p.member.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))
    )
    AND p.isVisible = true
    AND p.isDeleted = false
    ORDER BY p.id DESC
    LIMIT :size
""")
	List<Post> searchPosts(
			@Param("keyword") String keyword,
			@Param("searchType") String searchType,
			@Param("cursorId") Long cursorId,
			@Param("size") int size
	);

	Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
