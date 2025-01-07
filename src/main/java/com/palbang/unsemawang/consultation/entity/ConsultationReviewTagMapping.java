package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "consultation_review_tag_mapping")
public class ConsultationReviewTagMapping extends BaseEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private ConsultationReviewTag tag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private ConsultationReview review;

	@Column(name = "registered_at", nullable = false)
	private LocalDateTime registeredAt;

	@Column(name = "is_deleted", nullable = false)
	@ColumnDefault("false")
	private Boolean isDeleted;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
}
