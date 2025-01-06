package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "fortune_result")
public class FortuneResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 운세 결과 ID

	//    @ManyToOne(fetch = FetchType.LAZY)
	//    @JoinColumn(name = "member_id", nullable = false) // 일반 회원 ID와 다대일 관계
	//    private Member member;

	@Column(name = "guest_id", nullable = false)
	private String guestId; // 비회원 ID

	//    @ManyToOne(fetch = FetchType.LAZY)
	//    @JoinColumn(name = "user_fortune_information_id", nullable = false) // 사주 정보와 다대일 관계
	//    private FortuneUser fortuneUser;

	//    @ManyToOne(fetch = FetchType.LAZY)
	//    @JoinColumn(name = "fortune_category_id", nullable = false) // 운세 유형과 다대일 관계
	//    private FortuneCategory fortuneCategory;

	@Column(name = "fortune_result", nullable = false, columnDefinition = "TEXT")
	private String fortuneResult; // 운세 결과

	@Column(name = "registered_at", nullable = false)
	private LocalDateTime registeredAt; // 생성 일시
}