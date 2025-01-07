package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.consultation.constant.ReservationState;

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
@Table(name = "consultation_reservation")
public class ConsultationReservation extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "customer_id")
	// private GeneralMember generalMember;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "expert_id")
	// private Expert expert;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kind_id")
	private ConsultationKind kind;

	@Column(name = "consultation_category", nullable = false)
	private String consultationCategory;

	@Column(name = "start_at", nullable = false)
	private LocalDateTime startAt;

	@Column(name = "end_at", nullable = false)
	private LocalDateTime endAt;

	@Column(name = "request_message")
	private String requestMessage;

	@Column(name = "head_count", nullable = false)
	@ColumnDefault("1")
	private Integer headCount;

	@Column(name = "duration_time", nullable = false)
	@ColumnDefault("60")
	private Integer durationTime;

	@Column(name = "base_price")
	@ColumnDefault("0")
	private Integer basePrice;

	@Column(name = "discount_price")
	@ColumnDefault("0")
	private Integer discountPrice;

	@Column(name = "amount_price")
	@ColumnDefault("0")
	private Integer amountPrice;

	@Column(name = "is_prepaid")
	@ColumnDefault("false")
	private Boolean isPrepaid;

	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'PENDING'")
	private ReservationState state;

	@Column(name = "is_canceled", nullable = false)
	@ColumnDefault("false")
	private Boolean isCanceled;

	@Column(name = "is_review", nullable = false)
	@ColumnDefault("false")
	private Boolean isReview;

	// 해당 컬럼은 있는게 좋을지 없는게 나을지 판단 후에 수정 예정
	// @Column(name = "state_update_at", nullable = false)
	// private LocalDateTime stateUpdatedAt;

	@Column(name = "member_cancel_until")
	private LocalDateTime memberCancelUntil;

	@Column(name = "expert_cancel_until")
	private LocalDateTime expertCancelUntil;

	@Column(name = "requested_at", nullable = false)
	private LocalDateTime requestedAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

}
