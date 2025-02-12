package com.palbang.unsemawang.fcm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fcm.constant.BrowserType;
import com.palbang.unsemawang.fcm.constant.DeviceType;
import com.palbang.unsemawang.fcm.entity.FcmToken;

@Repository
public interface FcmRepository extends JpaRepository<FcmToken,String> {
	List<FcmToken> findByMemberIdAndFcmToken(String memberId, String fcmToken);

	@Query("SELECT f.fcmToken FROM FcmToken f WHERE f.member.id = :memberId AND f.isActive = true")
	List<String> findByMemberIdAndIsActive(@Param("memberId") String memberId);
	boolean existsByMemberId(String memberId);
	void deleteByMemberId(String memberId);

	Optional<FcmToken> findFirstByFcmTokenAndIsActive(String fcmToken,boolean isActive);
}
