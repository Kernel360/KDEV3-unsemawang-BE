package com.palbang.unsemawang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {
	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private Region region;

	private StaticCredentialsProvider awsCredentialsProvider() {
		return StaticCredentialsProvider.create(
			AwsBasicCredentials.create(accessKey, secretKey)
		);
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
			.region(region)
			.credentialsProvider(awsCredentialsProvider())
			//                .credentialsProvider(DefaultCredentialsProvider.create())
			.httpClientBuilder(ApacheHttpClient.builder())
			.build();
	}

	@Bean
	public S3Presigner s3Presigner() {
		return S3Presigner.builder()
			.region(region)
			.credentialsProvider(awsCredentialsProvider())
			//                .credentialsProvider(DefaultCredentialsProvider.create())
			.build();
	}

}
