package com.palbang.unsemawang.chemistry.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FiveElements {
	PLUS_TREE("목", "甲", "갑", true),
	MINUS_TREE("목", "乙", "을", false),
	PLUS_FIRE("화", "丙", "병", true),
	MINUS_FIRE("화", "丁", "정", false),
	PLUS_SOIL("토", "戊", "무", true),
	MINUS_SOIL("토", "己", "기", false),
	PLUS_GOLD("금", "庚", "경", true),
	MINUS_GOLD("금", "辛", "신", false),
	PLUS_WATER("수", "壬", "임", true),
	MINUS_WATER("수", "癸", "계", false);

	private static final Map<String, FiveElements> lookup = new HashMap<>();

	static {
		for (FiveElements fe : FiveElements.values()) {
			lookup.put(fe.reading, fe);
		}
	}

	private final String element;
	private final String hanja;
	private final String reading;
	private final boolean isYang;

	public static FiveElements fromReading(String reading) {
		return lookup.get(reading);
	}
}