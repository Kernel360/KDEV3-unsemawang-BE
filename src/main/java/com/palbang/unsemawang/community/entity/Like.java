package com.palbang.unsemawang.community.entity;

import java.time.LocalDateTime;

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
@Table(name = "like")
public class Like {
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

	@Column(name = "is_liked", nullable = false)
	@Builder.Default
	private Boolean isLiked = true; // 좋아요 상태

	@Column(name = "registered_at", updatable = false)
	@Builder.Default
	private LocalDateTime registeredAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}