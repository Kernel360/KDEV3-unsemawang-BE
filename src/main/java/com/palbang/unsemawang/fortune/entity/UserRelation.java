package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class UserRelation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment 설정
	private Long id;

	@Column(name = "relation_name", nullable = false)
	private String relationName; // 관계명

	@Column(name = "relation_detail_name", nullable = false)
	private String relationDetailName; // 관계 상세명

	@Column(name = "registered_at", nullable = false, updatable = false)
	private LocalDateTime registeredAt; // 생성일시

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt; // 수정일시

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부 (기본값: false)

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제일시
}
