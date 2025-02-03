package com.palbang.unsemawang.community.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

	Optional<Post> findByIdAndMember(Long id, Member member);

	@Query("SELECT p FROM Post p WHERE p.id = :postId AND p.member = :member AND p.isDeleted = false")
	Optional<Post> findByMemberIsNotDeleted(Long postId, Member member);

	// 조회수 증가
	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
	void incrementViewCount(@Param("postId") Long postId);

	@Query("SELECT p.registeredAt FROM Post p WHERE p.id = :id")
	LocalDateTime findRegisteredAtById(@Param("id") Long id);

	Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
