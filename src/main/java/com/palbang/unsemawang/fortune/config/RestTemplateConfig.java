package com.palbang.unsemawang.fortune.config;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	private static final Logger log = LoggerFactory.getLogger(RestTemplateConfig.class);

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.additionalInterceptors(logRequestResponseInterceptor()) // Interceptor 추가
			.build();
	}

	private ClientHttpRequestInterceptor logRequestResponseInterceptor() {
		return (request, body, execution) -> {
			// 요청 정보 로깅
			log.debug("Request URI: {}", request.getURI());
			log.debug("Request Method: {}", request.getMethod());
			log.debug("Request Headers: {}", request.getHeaders());
			log.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));

			// 응답 처리
			var response = execution.execute(request, body);

			// 응답 정보 로깅
			log.debug("Response Status Code: {}", response.getStatusCode());
			log.debug("Response Headers: {}", response.getHeaders());

			return response;
		};
	}
}