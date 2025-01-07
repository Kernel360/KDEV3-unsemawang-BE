package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class FortuneUserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본 키

	@ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정
	@JoinColumn(name = "member_id", nullable = true) // 외래 키 매핑
	private Member member; // Member와의 연관 관계

	@Column(name = "guest_id", nullable = true)
	private String guestId; // 비회원 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relation_id", nullable = false)
	private UserRelation relation;

	@Column(name = "nickname", nullable = false)
	private String nickname; // 닉네임

	@Column(name = "birthdate", nullable = false)
	private LocalDate birthdate; // 생년월일

	@Column(name = "birthtime", nullable = true)
	private LocalTime birthtime; // 태어난 시간

	@Column(name = "sex", nullable = false)
	private char sex; // 성별 ('F', 'M')

	@Column(name = "registered_at", nullable = false, updatable = false)
	private LocalDateTime registeredAt; // 생성일시

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt; // 수정일시

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}
