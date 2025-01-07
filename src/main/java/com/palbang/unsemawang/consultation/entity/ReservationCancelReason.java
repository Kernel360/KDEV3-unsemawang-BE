package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.consultation.constant.MemberType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "reservation_cancel_reason")
public class ReservationCancelReason extends BaseEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "description")
	private String description;

	@Column(name = "member_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberType memberType;

	@Column(name = "is_visible", nullable = false)
	@ColumnDefault("true")
	private Boolean isVisible;

	@Column(name = "registered_at", nullable = false)
	private LocalDateTime registeredAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "is_deleted", nullable = false)
	@ColumnDefault("false")
	private Boolean isDeleted;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
