package com.palbang.unsemawang.fcm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fcm.dto.request.FcmNotificationRequest;
import com.palbang.unsemawang.fcm.dto.request.FcmTokenRegiRequest;
import com.palbang.unsemawang.fcm.service.FcmService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name="fcm 토큰 등록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

	private final FcmService fcmService;

	@Operation(
		summary = "FCM 토큰 저장",
		description = "알림 설정한 클라이언트의 FCM 토큰을 저장합니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@PostMapping("/register-token")
	public ResponseEntity<String> registerToken(@AuthenticationPrincipal CustomOAuth2User auth, @RequestBody @Valid FcmTokenRegiRequest request) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}
		//FCM 토큰 저장
		fcmService.saveToken(auth.getId(), request.getFcmToken(), request.getDeviceType(), request.getBrowserType());

		return ResponseEntity.ok("Token saved!");
	}

	//FCM 서버에 푸시 메시지 전송 요청
	@Operation(
		summary = "FCM 서버에 푸시 메시지 전송 요청",
		description = "FCM 서버에 푸시 메시지 전송을 요청 합니다."
	)
	@ApiResponse(responseCode = "200", description = "푸시 메시지 전송 요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 형식이 잘못됨 (잘못된 토큰, 필드 누락, API 키 문제 등) -INVALID_ARGUMENT")
	@ApiResponse(responseCode = "404", description = "FCM 토큰이 만료됨 (앱 삭제, 데이터 초기화, 오래 사용 안 한 경우) -UNREGISTERED")
	@PostMapping("/send")
	public String sendNotification(@AuthenticationPrincipal CustomOAuth2User auth, @RequestBody @Valid
		FcmNotificationRequest request) {

		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}

		try {
			return fcmService.sendPushMessage(request);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}
	@Operation(
		summary = "FCM 토큰 조회",
		description = "로그인한 회원의 FCM 토큰을 조회합니다."
	)
	@GetMapping("/token")
	public ResponseEntity<String> getFcmToken(@AuthenticationPrincipal CustomOAuth2User auth) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}
		String fcmToken = fcmService.getFcmToken(auth.getId());
		return ResponseEntity.ok(fcmToken);
	}

	@Operation(
		summary = "FCM 토큰 삭제",
		description = "회원의 FCM 토큰을 삭제합니다."
	)
	@DeleteMapping("/token")
	public ResponseEntity<Void> deleteFcmToken(@AuthenticationPrincipal CustomOAuth2User auth) {
		if (auth == null || auth.getId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_TOKEN);
		}
		fcmService.deleteFcmToken(auth.getId());
		return ResponseEntity.noContent().build();
	}


}
