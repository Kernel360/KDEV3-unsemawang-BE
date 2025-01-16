package com.palbang.unsemawang.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.member.entity.Member;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findByIdAndMember(Long id, Member member);
}
