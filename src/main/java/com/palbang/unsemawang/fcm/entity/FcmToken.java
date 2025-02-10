package com.palbang.unsemawang.fcm.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.fcm.constant.BrowserType;
import com.palbang.unsemawang.fcm.constant.DeviceType;
import com.palbang.unsemawang.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FcmToken extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)  // N:1 관계 설정
	@JoinColumn(name = "member_id", nullable = false)  // 외래 키(FK)
	private Member member;

	@Column(nullable = false, unique = true)
	private String fcmToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceType deviceType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BrowserType browserType;  // 브라우저 타입

	@Column(name = "updated_at", nullable = true)
	private LocalDateTime updatedAt;

	@Column(name = "registeredAt", nullable = true)
	private LocalDateTime registeredAt;
}
