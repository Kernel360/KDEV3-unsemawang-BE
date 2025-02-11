package com.palbang.unsemawang.fcm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.fcm.constant.BrowserType;
import com.palbang.unsemawang.fcm.constant.DeviceType;
import com.palbang.unsemawang.fcm.entity.FcmToken;

@Repository
public interface FcmRepository extends JpaRepository<FcmToken,String> {
	List<FcmToken> findByFcmToken(String FcmToken);
	List<FcmToken> findByMemberId(String memberId);
	List<FcmToken> findByMemberIdAndDeviceType(String memberId, DeviceType deviceType);
	List<FcmToken> findByMemberIdAndDeviceTypeAndBrowserType(String memberId, DeviceType deviceType, BrowserType browserType);

	boolean existsByMemberId(String memberId);
	void deleteByMemberId(String memberId);
}
