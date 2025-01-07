package com.palbang.unsemawang.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class HolidaySchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expert_id")
	private Member expert;

	@Column(nullable = false)
	private LocalDate startAt;

	@Column(nullable = false)
	private LocalDate endAt;

	@Column(nullable = false)
	private String reason; // enum과 고민

	@Column(nullable = false)
	private Boolean isVisible;

	@Column(nullable = false)
	private LocalDateTime registeredAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

}
