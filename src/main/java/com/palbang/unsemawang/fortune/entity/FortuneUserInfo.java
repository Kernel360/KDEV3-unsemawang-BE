package com.palbang.unsemawang.fortune.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.palbang.unsemawang.common.entity.BaseEntity;
import com.palbang.unsemawang.common.util.saju.SajuCalculator;
import com.palbang.unsemawang.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "fortune_user_info")
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

	@Column(name = "hour")
	private int hour; // 태어난 시간

	@Column(name = "sex", nullable = false)
	private char sex; // 성별 ('F', 'M')

	@Column(name = "youn", nullable = false)
	private int youn;

	@Column(name = "solunar", nullable = false)
	private String solunar;

	@Column(name = "day_gan")
	private String dayGan;

	@Column(name = "day_zhi")
	private String dayZhi;

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

	public void deleted() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	public void updateFortuneInfo(
		UserRelation relation,
		String nickname,
		int year,
		int month,
		int day,
		int hour,
		char sex,
		int youn,
		String solunar
	) {
		this.relation = relation; // 관계 변경
		this.nickname = nickname; // 닉네임 수정
		this.year = year; // 생년 수정
		this.month = month; // 생월 수정
		this.day = day; // 생일 수정
		this.hour = hour; // 태어난 시간 수정
		this.sex = sex; // 성별 수정
		this.youn = youn; // 윤년 여부 수정
		this.solunar = solunar; // 양력/음력 수정
		this.updatedAt = LocalDateTime.now(); // 수정 시간 갱신
	}

	public void updateFortuneNickname(String nickname) {
		this.nickname = nickname;
		this.updatedAt = LocalDateTime.now(); // 수정 시간 갱신
	}

	public void updateDayGanZhiFromBirthday() {
		boolean isLunar = solunar.equals("lunar");
		boolean isIntercalation = youn == 1;

		LocalDate lunarBirthday = SajuCalculator.getSolarDate(year, month, day, isLunar, isIntercalation);

		this.dayGan = SajuCalculator.getDayGan(lunarBirthday);
		this.dayZhi = SajuCalculator.getDayZhi(lunarBirthday);
	}
}
