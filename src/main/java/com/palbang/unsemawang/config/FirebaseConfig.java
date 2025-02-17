package com.palbang.unsemawang.config;

import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {
	private FirebaseApp firebaseApp;

	@PostConstruct
	public FirebaseApp initializeFcm() throws IOException {
		String credentialsPath;
		// HTTP/1.1을 사용하도록 NetHttpTransport 적용
		HttpTransport httpTransport = new ApacheHttpTransport();

		// 환경 변수 GOOGLE_APPLICATION_CREDENTIALS 확인
		String envVar = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

		if (envVar != null && !envVar.isEmpty()) {
			// 환경 변수로 설정된 경로 사용
			credentialsPath = envVar;
		} else {
			// 로컬 환경일 경우 ClassPathResource 사용
			credentialsPath = "firebase/serviceAccountKey.json";
		}

		// 파일 입력 스트림으로 Firebase 인증 파일 로드
		InputStream serviceAccount;
		if (credentialsPath.startsWith("firebase")) {
			// 로컬 환경에서는 ClassPathResource 사용
			ClassPathResource resource = new ClassPathResource(credentialsPath);
			serviceAccount = resource.getInputStream();
		} else {
			// 컨테이너 환경에서는 파일 경로로 직접 읽기
			serviceAccount = new FileInputStream(credentialsPath);
		}

		// Firebase 옵션 설정
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setHttpTransport(httpTransport)
			.build();

		if (FirebaseApp.getApps().isEmpty()) {
			firebaseApp = FirebaseApp.initializeApp(options);
			log.info("FirebaseApp initialized");
		} else {
			firebaseApp = FirebaseApp.getInstance();
			log.info("FirebaseApp already initialized");
		}

		return firebaseApp;
	}

	@Bean
	public FirebaseMessaging initFirebaseMessaging() {
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}