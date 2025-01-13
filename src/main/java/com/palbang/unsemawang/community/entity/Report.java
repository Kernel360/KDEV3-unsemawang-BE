package com.palbang.unsemawang.community.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.community.constant.ReportReason;
import com.palbang.unsemawang.community.constant.ReportStatus;
import com.palbang.unsemawang.community.constant.TargetType;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "target_type", nullable = false)
	private TargetType targetType; // 대상 유형 (POST, COMMENT)

	@Column(name = "target_id", nullable = false)
	private Long targetId; // 대상 ID (게시글 또는 댓글)

	@Column(name = "reason", columnDefinition = "TEXT")
	private String reason; // 신고 사유

	@Enumerated(EnumType.STRING)
	@Column(name = "report_category")
	private ReportReason reportCategory;

	@Column(name = "registered_at", updatable = false)
	@Builder.Default
	private LocalDateTime registeredAt = LocalDateTime.now(); // 신고 등록 시간

	@Enumerated(EnumType.STRING)
	@Column(name = "report_status")
	private ReportStatus reportStatus; // 신고 상태 : 처리 중, 처리 완료 등
}