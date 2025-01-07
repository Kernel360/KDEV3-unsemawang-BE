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
@Table(name = "fortune_favorite")
public class FortuneFavorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 콘텐츠 찜 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fortune_content_id", nullable = false) // FortuneContent와 다대일 관계
	private FortuneContent fortuneContent;

	@Column(name = "member_id", nullable = false)
	private String memberId; // 일반회원 ID (단순 컬럼)

	@Column(name = "registered_at", nullable = false)
	private LocalDateTime registeredAt; // 생성일시

	@Builder.Default
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부 (기본값: false)

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}