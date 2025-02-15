package com.palbang.unsemawang.fcm.dto.request;

import java.time.LocalDateTime;

import com.palbang.unsemawang.fcm.constant.BrowserType;
import com.palbang.unsemawang.fcm.constant.DeviceType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRegiRequest {
	@Schema(description = "FCM토큰", required = true, example = "ftyp3szqbFgZoWgtJfK-vI:APA91bHaDYCc42JWuwhc-1wSifRQAbOMz_x3ofF5pIhe6eivUNi9-Yop7BD8bePcjevZnx02JwpeOWGO2J-PQjjJjvWtoFLe3My1x5VNDlBKaJIma10Qbww")
	private String fcmToken;
	@Schema(description = "기기타입", required = false, example = "WEB")
	private DeviceType deviceType;
	@Schema(description = "브라우저종류", required = false, example = "CHROME")
	private BrowserType browserType;
}
