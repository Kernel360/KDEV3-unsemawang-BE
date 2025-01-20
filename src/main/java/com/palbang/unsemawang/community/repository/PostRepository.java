package com.palbang.unsemawang.community.repository;

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
	Optional<Post> findBuMemberIsNotDeleted(Long postId, Member member);

	// 조회수 증가
	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
	void incrementViewCount(@Param("postId") Long postId);

	// 카테고리에 따른 최신 게시글 가져오기
	@Query("""
			SELECT p FROM Post p
			WHERE (p.id < :cursorId OR :cursorId IS NULL)
			AND p.communityCategory = :category
			AND p.isVisible = true
			AND p.isDeleted = false
			ORDER BY p.id DESC
		""")
	List<Post> findLatestPostsByCategory(
		@Param("category") CommunityCategory category,
		@Param("cursorId") Long cursorId,
		Pageable pageable // Spring Data JPA Pageable 사용
	);

	// 인기 게시글 가져오기
	@Query("""
		    SELECT p FROM Post p
		    WHERE p.isVisible = true
		    AND p.isDeleted = false
		    AND (p.id < :cursorId OR :cursorId IS NULL)
		    ORDER BY (p.viewCount * 7) + (p.likeCount * 3) DESC, p.id DESC
		""")
	List<Post> findPopularPosts(
		@Param("cursorId") Long cursorId,
		Pageable pageable
	);
}
