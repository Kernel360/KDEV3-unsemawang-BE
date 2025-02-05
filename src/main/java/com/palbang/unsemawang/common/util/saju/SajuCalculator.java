package com.palbang.unsemawang.common.util.saju;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.github.usingsky.calendar.KoreanLunarCalendar;

public class SajuCalculator {

	// 천간 (10)과 지지 (12)
	private static final String[] HEAVENLY_STEMS = {"갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"};
	private static final String[] EARTHLY_BRANCHES = {"자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"};

	private static final LocalDate BASE_DATE = LocalDate.of(1900, 1, 1);

	public static String getDayGan(LocalDate targetDate) {

		long daysPassed = ChronoUnit.DAYS.between(BASE_DATE, targetDate);

		return HEAVENLY_STEMS[(int)(daysPassed % 10)];
	}

	public static String getDayZhi(LocalDate targetDate) {

		long daysPassed = ChronoUnit.DAYS.between(BASE_DATE, targetDate);

		return EARTHLY_BRANCHES[(int)((daysPassed - 2) % 12)];
	}

	public static LocalDate getSolarDate(int year, int month, int day, boolean isLunar, boolean isIntercalation) {
		if (isLunar) {
			KoreanLunarCalendar calendar = KoreanLunarCalendar.getInstance();
			calendar.setLunarDate(year, month, day, isIntercalation);
			year = calendar.getSolarYear();
			month = calendar.getSolarMonth();
			day = calendar.getSolarDay();
		}
		return LocalDate.of(year, month, day);
	}

}
