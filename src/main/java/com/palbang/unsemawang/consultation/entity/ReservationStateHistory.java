package com.palbang.unsemawang.consultation.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.consultation.constant.MemberType;
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
@Table(name = "reservation_state_history")
public class ReservationStateHistory extends BaseEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private ConsultationReservation reservation;

	@Column(name = "previous_state")
	@Enumerated(EnumType.STRING)
	private ReservationState previousState;

	@Column(name = "current_state")
	@Enumerated(EnumType.STRING)
	private ReservationState currentState;

	@Column(name = "changer_type")
	private MemberType changerType;

	@Column(name = "reason")
	private String reason;

	@Column(name = "changed_at", nullable = false)
	private LocalDateTime changedAt;
}
