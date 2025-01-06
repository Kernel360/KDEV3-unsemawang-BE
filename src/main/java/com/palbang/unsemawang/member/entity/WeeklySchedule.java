package com.palbang.unsemawang.member.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.palbang.unsemawang.member.constant.DayOfTheWeek;

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
public class WeeklySchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expert_id")
	private Member expert;

	@Enumerated(EnumType.STRING)
	private DayOfTheWeek day;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private Boolean isVisible;

	@Column(nullable = false)
	private LocalDateTime registeredAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

}
