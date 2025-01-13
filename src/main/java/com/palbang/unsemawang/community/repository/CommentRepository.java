package com.palbang.unsemawang.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
