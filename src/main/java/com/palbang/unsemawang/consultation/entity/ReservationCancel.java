package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.palbang.unsemawang.consultation.constant.CancelState;
import com.palbang.unsemawang.consultation.constant.MemberType;

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

@Entity
public class ReservationCancel {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "canceller_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberType cancellerType;

	@Column(name = "canceller_id", nullable = false)
	private Long cancellerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id")
	private ReservationCancelReason reason;

	@Column(name = "reason_detail", nullable = false)
	private String reasonDetail;

	@Column(name = "refund_amount_price")
	@ColumnDefault("0")
	private Integer refundPrice;

	@Column(name = "state", nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("REQUESTED")
	private CancelState state;

	@Column(name = "requested_at", nullable = false)
	private LocalDateTime requestedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "state_updated_at", nullable = false)
	private LocalDateTime stateUpdatedAt;
}
