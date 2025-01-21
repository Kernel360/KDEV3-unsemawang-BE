package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "fortune_content")
public class FortuneContent extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 운세 컨텐츠 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private FortuneCategory fortuneCategory;

	@Column(name = "name_en", nullable = false)
	private String nameEn; // 운세 유형명

	@Column(name = "name_kr", nullable = false)
	private String nameKo;

	@Column(name = "short_description")
	private String shortDescription; // 짧은 설명

	@Column(name = "long_description")
	private String longDescription; // 짧은 설명

	@Column(name = "thumbnail_url")
	private String thumbnailUrl; // 썸네일 URL

	@Column(name = "path")
	private String path;

	@CreationTimestamp
	@Column(name = "registered_at", nullable = false, updatable = false)
	private LocalDateTime registeredAt; // 생성일시

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt; // 수정일시

	@Builder.Default
	@Column(name = "is_visible", nullable = false)
	private Boolean isVisible = true; // 표시 여부 (기본값: true)

	@Builder.Default
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부 (기본값: false)

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}