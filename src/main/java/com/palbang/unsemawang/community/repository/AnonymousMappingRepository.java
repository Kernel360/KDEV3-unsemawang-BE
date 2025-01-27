package com.palbang.unsemawang.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.community.entity.AnonymousMapping;

@Repository
public interface AnonymousMappingRepository extends JpaRepository<AnonymousMapping, Long> {
	Optional<AnonymousMapping> findByPostIdAndMemberId(Long postId, String memberId);

	long countByPostId(Long postId); // 게시글별 익명 이름 개수 카운트
}
