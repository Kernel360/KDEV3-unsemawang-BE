package com.palbang.unsemawang.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	private SecretKey secretKey;

	public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
		if (secret == null || secret.isEmpty()) {
			throw new IllegalArgumentException("JWT secret is not configured!");
		}
		//this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),  Jwts.SIG.HS256.key().build().getAlgorithm());
		this.secretKey = new SecretKeySpec(
			secret.getBytes(StandardCharsets.UTF_8), // 시크릿 키를 바이트 배열로 변환
			"HmacSHA256" // 알고리즘 설정
		);
	}

	public String getId(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("id", String.class);
	}

	public String getEmail(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("email", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}

	public boolean isExpired(String token) {
		try {
			//return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
			return Jwts.parser().setSigningKey(secretKey)
				.build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			// 토큰이 만료된 경우
			return true;
		} catch (Exception e) {
			// 기타 예외 처리
			throw new RuntimeException("Invalid JWT token", e);
		}
	}

	public String createJTwt(String id, String email, String role, Long expiredMs) {
		return Jwts.builder()
			.claim("id", id)
			.claim("email", email)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}

}
