package com.palbang.unsemawang.chemistry.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum FiveElements {
	MINUS_FIRE("-화", "丁"),
	PLUS_FIRE("+화", "丙"),
	MINUS_TREE("-목", "乙"),
	PLUS_TREE("+목", "甲"),
	MINUS_GOLD("-금", "辛"),
	PLUS_GOLD("+금", "庚"),
	MINUS_SOIL("-토", "己"),
	PLUS_SOIL("+토", "戊"),
	MINUS_WATER("-수", "癸"),
	PLUS_WATER("+수", "壬");

	private final String korean;
	private final String chinese;

	// 상생 관계
	private static final Map<String, String> good = Map.of(
		"화", "토",
		"토", "금",
		"금", "수",
		"수", "목",
		"목", "화"
	);

	// 상극 관계
	private static final Map<String, String> bad = Map.of(
		"목", "토",
		"토", "수",
		"수", "화",
		"화", "금",
		"금", "목"
	);

	FiveElements(String korean, String chinese) {
		this.korean = korean;
		this.chinese = chinese;
	}

	public static List<String> getKoreans() {
		List<String> koreans = new ArrayList<>();

		for (FiveElements wuXing : values()) {
			koreans.add(wuXing.korean);
		}

		return koreans;
	}

	// 궁합 점수 계산 메서드
	public static int getChemistryScore(String e1, String e2) {
		if (valueOf(e1) == valueOf(e2)) {
			return 1;
		}

		if (good.get(e2).equals(e1)) {
			return 5;
		}

		if (bad.get(e2).equals(e1)) {
			return -5;
		}

		return 0;
	}
}