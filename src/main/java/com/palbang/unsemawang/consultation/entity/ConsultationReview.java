package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ConsultationReview {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "reviewer_id")
	// private GeneralMember generalMember;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "reservation_id")
	// private ConsultationReservation reservation;

	@Column(name = "score", nullable = false)
	private Integer score;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "goodCount", nullable = false)
	@ColumnDefault("0")
	private Integer goodCount;

	@Column(name = "reportCount", nullable = false)
	@ColumnDefault("0")
	private Integer reportCount;

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
