package com.palbang.unsemawang.fortune.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;

@Repository
public interface FortuneUserInfoRepository extends JpaRepository<FortuneUserInfo, Long> {
	@Query("SELECT f FROM FortuneUserInfo f WHERE f.member.id = :id AND f.isDeleted = false")
	List<FortuneUserInfo> findByMemberId(String id);

	@Query("SELECT f FROM FortuneUserInfo f " +
		"JOIN f.relation r " +
		"WHERE f.member.id = :id AND r.relationName = :relationName AND f.isDeleted = false")
	List<FortuneUserInfo> findByMemberIdAndRelation(@Param("id") String id, @Param("relationName") String relationName);

}
