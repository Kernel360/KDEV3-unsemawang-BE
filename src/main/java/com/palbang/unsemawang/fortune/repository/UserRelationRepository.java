package com.palbang.unsemawang.fortune.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.UserRelation;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
	Optional<UserRelation> findByRelationName(String relationName);
}
