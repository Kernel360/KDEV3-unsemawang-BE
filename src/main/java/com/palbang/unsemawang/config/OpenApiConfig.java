package com.palbang.unsemawang.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		contact = @Contact(
			name = "palbang",
			url = "https://github.com/Kernel360/KDEV3-unsemawang-BE"
		),
		description = "운세 마왕 API 문서",
		title = "Unsemawang API Specification - palbang",
		version = "1.0"
	),
	servers = {
		@Server(
			description = "dev",
			url = "https://dev.unsemawang.com/"),
		@Server(
			description = "Local ENV",
			url = "http://localhost:8080"
		)
		/* 프로덕트 url 추가 후 주석 해제 예정 */
		// , @Server(
		// 	description = "Product ENV",
		// 	url = "https://~~~"
		// )
	}
)
public class OpenApiConfig {

}
