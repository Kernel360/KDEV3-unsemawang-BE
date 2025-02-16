package com.palbang.unsemawang.fortune.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;

@Repository
public interface FortuneUserInfoRepository extends JpaRepository<FortuneUserInfo, Long> {
	@Query("SELECT f FROM FortuneUserInfo f WHERE f.member.id = :id AND f.isDeleted = false")
	List<FortuneUserInfo> findByMemberId(String id);

	@Query("""
		SELECT f FROM FortuneUserInfo f
		JOIN f.relation r
		WHERE f.member.id = :id AND r.relationName = :relationName AND f.isDeleted = false
		""")
	List<FortuneUserInfo> findByMemberIdAndRelation(@Param("id") String id, @Param("relationName") String relationName);

	boolean existsByMemberIdAndRelationId(String memberId, Long relationId);

	// FortuneUserInfo에서 dayGan 조회
	@Query("SELECT f FROM FortuneUserInfo f WHERE f.member.id = :memberId AND f.relation.id = 1")
	Optional<FortuneUserInfo> findByMemberIdRelationIdIsOne(String memberId);

	// 테스트용: dayGan 또는 dayZhi가 null인 데이터 조회
	List<FortuneUserInfo> findByDayGanIsNullOrDayZhiIsNull();
}
