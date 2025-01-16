package com.palbang.unsemawang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

/* 테스트용 SecurityContext 생성을 위한 어노테이션 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockSecurityContextFactory.class) // SecurityContext 생성 클래스 등록
public @interface WithCustomMockUser {

	String memberId() default "testuser";

	String role() default "GENERAL";

	String email() default "testuser@gmail.com";
}
