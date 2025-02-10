package com.palbang.unsemawang.config;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
		// 프로젝트 내부에 있는 JSON 파일 로드
		ClassPathResource resource = new ClassPathResource("firebase/serviceAccountKey.json");
		InputStream serviceAccount = resource.getInputStream();

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.build();

		firebaseApp = FirebaseApp.initializeApp(options);
		log.info("FirebaseApp initialized");
		return firebaseApp;
	}
	@Bean
	public FirebaseMessaging initFirebaseMessaging() {
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}