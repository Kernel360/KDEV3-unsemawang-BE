package com.palbang.unsemawang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns(
				"http://localhost:3000",
				"http://deploy-kdev-3-unsemawang-fe.vercel.app",
				"https://deploy-kdev-3-unsemawang-fe.vercel.app",
				"https://www.unsemawang.com/",
				"http://www.unsemawang.com/")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
