package com.palbang.unsemawang.fcm.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fcm.constant.BrowserType;
import com.palbang.unsemawang.fcm.constant.DeviceType;
import com.palbang.unsemawang.fcm.dto.request.FcmNotificationRequest;
import com.palbang.unsemawang.fcm.entity.FcmToken;
import com.palbang.unsemawang.fcm.repository.FcmRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmService {
	private final FcmRepository fcmRepository;
	private final MemberRepository memberRepository;

	@Transactional  // 변경 감지를 위해 추가
	public boolean saveToken(String memberId, String fcmToken, DeviceType deviceType, BrowserType browserType) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID, "회원을 찾을 수 없습니다"));
		// 기존 토큰이 있는지 확인 (중복 방지)
		// 토큰이 존재하는지 체크 이미 존재하면... 이미 등록된 fcm 토큰입니다. 라고 안내..

		List<FcmToken> existingToken = fcmRepository.findByMemberIdAndFcmToken(memberId,fcmToken);
		if (existingToken.isEmpty()) {
			// 새로운 FCM 토큰 저장
			FcmToken newToken = FcmToken.builder()
				.member(member)
				.fcmToken(fcmToken)
				.deviceType(deviceType)
				.browserType(browserType)
				.registeredAt(LocalDateTime.now())
				.isActive(true)
				.build();

			fcmRepository.save(newToken);
			return true;  // 새로운 토큰이 추가됨
		} else {
			//기존 토큰이 있다면 업데이트
			FcmToken token = existingToken.get(0);
			if (!token.getIsActive()) {  // isActive가 false면 true로 변경
				token.setIsActive(true);
			}
			token.setUpdatedAt(LocalDateTime.now());
			return false;  // 기존 토큰이 업데이트됨
		}
	}

	public String sendPushMessage(FcmNotificationRequest request) throws FirebaseMessagingException {
		String token = request.getFcmToken();
		String title = request.getTitle();
		String body = request.getBody();
		String url = request.getUrl();

		// FCM 메시지 요청 생성
		Message message = Message.builder()
			.setToken(token) // 특정 기기의 FCM 토큰
			.putData("title", title)
			.putData("body", body)
			.putData("click_action", url)
			.putData("image", "https://www.unsemawang.com/icon/icon_192.png")
			.build();

		// FCM 메시지 전송
		return FirebaseMessaging.getInstance().send(message);
	}

	public String sendPushNotification(FcmNotificationRequest request) throws FirebaseMessagingException {
		String token = request.getFcmToken();
		String title = request.getTitle();
		String body = request.getBody();
		String url = request.getUrl();

		Notification notification = Notification.builder()
			.setTitle(title)
			.setBody(body)
			.build();

		// FCM 메시지 요청 생성
		Message message = Message.builder()
			.setToken(token) // 특정 기기의 FCM 토큰
			.setNotification(notification)
			.putData("click_action", url)
			.putData("image", "https://www.unsemawang.com/icon/icon_192.png")
			.build();

		// FCM 메시지 전송
		return FirebaseMessaging.getInstance().send(message);
	}

	public List<String> getFcmToken(String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID, "회원을 찾을 수 없습니다"));
		List<String> fcmTokenList = fcmRepository.findByMemberIdAndIsActive(memberId);
		if (fcmTokenList.isEmpty()) {
			return Collections.emptyList();
		}else{
			return fcmTokenList;
		}
	}

	@Transactional
	public void deleteFcmToken(String memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID, "회원을 찾을 수 없습니다"));

		boolean exists = fcmRepository.existsByMemberId(memberId);
		if (!exists) {
			throw new GeneralException(ResponseCode.NOT_EXIST_FCM_TOKEN, "FCM 토큰이 존재하지 않습니다.");
		}
		fcmRepository.deleteByMemberId(memberId);
	}

	@Transactional
	public void deactivateFcmToken(String memberId,String fcmToken){
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID, "회원을 찾을 수 없습니다"));

		// 활성화된 FCM 토큰 조회 (Optional 사용)
		FcmToken fcmTokenEntity = fcmRepository.findFirstByFcmTokenAndIsActive(fcmToken, true)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_ACTIVE_FCM_TOKEN, "활성화된 FCM 토큰이 존재하지 않습니다."));

		// 해당 FCM 토큰이 해당 회원의 것인지 검증
		if (!fcmTokenEntity.getMember().getId().equals(memberId)) {
			throw new GeneralException(ResponseCode.INVALID_FCM_TOKEN, "해당 FCM 토큰은 해당 회원의 것이 아닙니다.");
		}

		// 토큰 비활성화
		fcmTokenEntity.setIsActive(false);
	}
}