package com.palbang.unsemawang.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
