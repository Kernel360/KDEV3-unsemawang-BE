package com.palbang.unsemawang.chemistry.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FiveElements {
	PLUS_TREE("목", "木", "甲", "갑", true),
	MINUS_TREE("목", "木", "乙", "을", false),
	PLUS_FIRE("화", "火", "丙", "병", true),
	MINUS_FIRE("화", "火", "丁", "정", false),
	PLUS_SOIL("토", "土", "戊", "무", true),
	MINUS_SOIL("토", "土", "己", "기", false),
	PLUS_GOLD("금", "金", "庚", "경", true),
	MINUS_GOLD("금", "金", "辛", "신", false),
	PLUS_WATER("수", "水", "壬", "임", true),
	MINUS_WATER("수", "水", "癸", "계", false);

	private static final Map<String, FiveElements> lookup = new HashMap<>();

	static {
		for (FiveElements fe : FiveElements.values()) {
			lookup.put(fe.reading, fe);
		}
	}

	private final String element;
	private final String chinese;
	private final String dayGan;
	private final String reading;
	private final boolean isYang;

	public static FiveElements fromReading(String reading) {
		return lookup.get(reading);
	}

	// "갑" -> "木" 로 변경
	public static String convertToChinese(String reading) {
		FiveElements fe = lookup.get(reading);
		return fe.chinese;
	}
}