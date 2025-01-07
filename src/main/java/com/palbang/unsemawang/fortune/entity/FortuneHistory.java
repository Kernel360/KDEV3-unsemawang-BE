package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class FortuneHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 운세 히스토리 ID

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fortune_id", nullable = false, unique = true) // fortune_result와 1:1 관계
	private FortuneResult fortuneResult;

	@Column(name = "member_id", nullable = false)
	private String memberId; // 일반회원 ID

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_fortune_information_id", nullable = false)
	private FortuneUserInfo fortuneUserInfo;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false; // 삭제 여부 (기본값: false)

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt; // 삭제 일시
}
