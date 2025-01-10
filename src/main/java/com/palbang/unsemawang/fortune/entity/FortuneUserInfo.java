package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.member.entity.Member;

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

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class FortuneUserInfo extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relation_id", nullable = false)
	private UserRelation relation;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "year", nullable = false)
	private int year;

	@Column(name = "month", nullable = false)
	private int month;

	@Column(name = "day", nullable = false)
	private int day;

	@Column(name = "birthtime")
	private int birthtime; // 태어난 시간

	@Column(name = "sex", nullable = false)
	private char sex; // 성별 ('F', 'M')

	@Column(name = "youn", nullable = false)
	private int youn;

	@Column(name = "solunar", nullable = false)
	private String solunar;

	@Column(name = "registered_at", nullable = false, updatable = false)
	@Builder.Default
	private LocalDateTime registeredAt = LocalDateTime.now(); // 생성일시

	@Column(name = "updated_at", nullable = false)
	@Builder.Default
	private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시

	@Column(name = "is_deleted", nullable = false)
	@Builder.Default
	private Boolean isDeleted = false; // 삭제 여부

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}
