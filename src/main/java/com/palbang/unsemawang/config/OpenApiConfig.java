package com.palbang.unsemawang.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
	},
	security = @SecurityRequirement(name = "bearerAuth") // 👈 전역 보안 적용
)
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class OpenApiConfig {

}
