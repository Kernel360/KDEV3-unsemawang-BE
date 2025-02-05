package com.palbang.unsemawang.common.util.saju;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SajuCalculator {

	// 천간 (10)과 지지 (12)
	private static final String[] HEAVENLY_STEMS = {"갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"};
	private static final String[] EARTHLY_BRANCHES = {"자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"};

	// 일주(日柱) 계산 (1900년 1월 1일을 기준으로 계산)
	public static String getDayGanZhi(int year, int month, int day) {
		LocalDate baseDate = LocalDate.of(1900, 1, 1);
		LocalDate targetDate = LocalDate.of(year, month, day);
		long daysPassed = ChronoUnit.DAYS.between(baseDate, targetDate);
		String stem = HEAVENLY_STEMS[(int)(daysPassed % 10)];
		String branch = EARTHLY_BRANCHES[(int)((daysPassed - 2) % 12)];
		return stem + branch;
	}
}
