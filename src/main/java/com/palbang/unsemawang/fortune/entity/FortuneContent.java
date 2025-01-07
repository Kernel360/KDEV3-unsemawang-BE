package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.palbang.unsemawang.member.entity.FortuneCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fortune_content")
public class FortuneContent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 운세 컨텐츠 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fortune_category_id") // 운세 분야 ID와 다대일 관계
	private FortuneCategory fortuneCategory;

	@Column(name = "fortune_content_name", nullable = false)
	private String fortuneContentName; // 운세 유형명

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description; // 설명

	@Column(name = "path")
	private String path;

	@CreatedDate
	@Column(name = "registered_at", nullable = false)
	private LocalDateTime registeredAt; // 생성일시

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt; // 수정일시

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true; // 활성 여부 (기본값: true)

	@Builder.Default
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부 (기본값: false)

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}