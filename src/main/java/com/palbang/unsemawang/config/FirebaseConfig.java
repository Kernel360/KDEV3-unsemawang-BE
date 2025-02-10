package com.palbang.unsemawang.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	private FirebaseApp firebaseApp;

	@PostConstruct
	public FirebaseApp initializeFcm() throws IOException {
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.getApplicationDefault())
			.build();
		firebaseApp = FirebaseApp.initializeApp(options);
		System.out.println("initializeFcm");
		return firebaseApp;
	}
	@Bean
	public FirebaseMessaging initFirebaseMessaging() {
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}