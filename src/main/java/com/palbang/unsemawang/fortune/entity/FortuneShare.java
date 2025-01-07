package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "fortune_share")
public class FortuneShare extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 공유 ID

	@ManyToOne(fetch = FetchType.LAZY) // FortuneResult와 다대일 관계
	@JoinColumn(name = "fortune_id", nullable = false)
	private FortuneResult fortuneResult;

	@Column(name = "platform", nullable = false)
	private String platform; // 공유 플랫폼 (예: Facebook, Twitter 등)

	@Column(name = "shared_url", nullable = false)
	private String sharedUrl; // 공유된 링크

	@Column(name = "shared_at", nullable = false)
	private LocalDateTime sharedAt; // 공유 일시

	@Column(name = "expired_at", nullable = false)
	private LocalDateTime expiredAt; // 공유 링크 만료 일시

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true; // 공유 활성화 여부 (기본값: true)
}