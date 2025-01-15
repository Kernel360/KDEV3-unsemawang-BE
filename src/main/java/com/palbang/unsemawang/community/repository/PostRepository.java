package com.palbang.unsemawang.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findByIdAndMember(Long id, Member member);
	@Modifying
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
	void incrementViewCount(@Param("postId") Long postId);
}
