package com.palbang.unsemawang.chemistry.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;

public class ChemistryCalculator {
	private static final Map<String, Set<String>> good = new HashMap<>();
	private static final Map<String, Set<String>> bad = new HashMap<>();
	private static final Map<String, Set<String>> goodDayStemMatch = new HashMap<>();

	static final int SAME_ELEMENT_SCORE = 1; // 같은 오행
	static final int YIN_YANG_DIFFERENCE_BONUS = 2; // 음양이 다를 경우
	static final int GOOD_RELATIONSHIP_SCORE = 5; // 상생 관계
	static final int STRONG_GOOD_RELATIONSHIP_BONUS = 1; // 상생 관계 추가 보너스
	static final int BAD_RELATIONSHIP_SCORE = -5; // 상극 관계
	static final int STRONG_BAD_RELATIONSHIP_PENALTY = -2; // 상극 관계 추가 패널티
	static final int BAD_RELATIONSHIP_IS_SAME_YIN_YANG = -2;
	static final int GOOD_DAY_STEM_MATCH_BONUS = 8; // 일간합 관계
	static final int BAD_RELATIONSHIP_WITH_GOOD_DAY_STEM_BONUS = 3; // 상극 관계이지만 일간합 관계일 때 보너스

	static {
		addRelation(good, "화", "토");
		addRelation(good, "토", "금");
		addRelation(good, "금", "수");
		addRelation(good, "수", "목");
		addRelation(good, "목", "화");

		addRelation(bad, "목", "토");
		addRelation(bad, "토", "수");
		addRelation(bad, "수", "화");
		addRelation(bad, "화", "금");
		addRelation(bad, "금", "목");

		addDayStemMatch("갑", "기");
		addDayStemMatch("을", "경");
		addDayStemMatch("병", "신");
		addDayStemMatch("정", "임");
		addDayStemMatch("무", "계");
	}

	private static void addRelation(Map<String, Set<String>> map, String elementA, String elementB) {
		map.computeIfAbsent(elementA, k -> new HashSet<>()).add(elementB);
		map.computeIfAbsent(elementB, k -> new HashSet<>()).add(elementA); // 양방향 저장
	}

	private static void addDayStemMatch(String elementA, String elementB) {
		goodDayStemMatch.computeIfAbsent(elementA, k -> new HashSet<>()).add(elementB);
		goodDayStemMatch.computeIfAbsent(elementB, k -> new HashSet<>()).add(elementA);
	}

	public static int getChemistryScore(String e1, String e2) {
		FiveElements fe1 = FiveElements.fromReading(e1);
		FiveElements fe2 = FiveElements.fromReading(e2);

		if (fe1 == null || fe2 == null) {
			throw new GeneralException(ResponseCode.NOT_EXIST_TENGAN);
		}

		int score = 0;

		// 같은 오행 점수
		if (fe1.getElement().equals(fe2.getElement())) {
			score += SAME_ELEMENT_SCORE;
			if (fe1.isYang() != fe2.isYang()) {
				score += YIN_YANG_DIFFERENCE_BONUS; // 같은 오행인데 음양이 다르면 추가 점수
			}
		} else if (good.getOrDefault(fe1.getElement(), new HashSet<>()).contains(fe2.getElement())) {
			// 상생 관계
			score += GOOD_RELATIONSHIP_SCORE;
			if ((fe1.getElement().equals("수") && fe2.getElement().equals("목")) ||
				(fe1.getElement().equals("목") && fe2.getElement().equals("화")) ||
				(fe1.getElement().equals("토") && fe2.getElement().equals("금")) ||
				(fe2.getElement().equals("수") && fe1.getElement().equals("목")) ||
				(fe2.getElement().equals("목") && fe1.getElement().equals("화")) ||
				(fe2.getElement().equals("토") && fe1.getElement().equals("금"))) {
				score += STRONG_GOOD_RELATIONSHIP_BONUS; // 특히 좋은 상생 관계
			}
			if (fe1.isYang() != fe2.isYang()) {
				score += YIN_YANG_DIFFERENCE_BONUS; // 상생 관계에서 음양이 다르면 추가 점수
			}
		} else if (bad.getOrDefault(fe1.getElement(), new HashSet<>()).contains(fe2.getElement())) {
			// 상극 관계 점수
			score += BAD_RELATIONSHIP_SCORE;
			if ((fe1.getElement().equals("수") && fe2.getElement().equals("화")) ||
				(fe1.getElement().equals("화") && fe2.getElement().equals("금")) ||
				(fe2.getElement().equals("수") && fe1.getElement().equals("화")) ||
				(fe2.getElement().equals("화") && fe1.getElement().equals("금"))) {
				score += STRONG_BAD_RELATIONSHIP_PENALTY; // 특히 강한 상극 관계
			}
			if (fe1.isYang() == fe2.isYang()) {
				score += BAD_RELATIONSHIP_IS_SAME_YIN_YANG; // 상극 관계에서 양/음이 같으면 추가 감점
			}
		}

		// 일간합 점수 (좋은 궁합)
		if (goodDayStemMatch.getOrDefault(fe1.getReading(), new HashSet<>()).contains(fe2.getReading())) {
			score += GOOD_DAY_STEM_MATCH_BONUS;
		}

		// 상극 관계인데도 불구하고 일간합이면 감점 완화 (+3점)
		if (bad.getOrDefault(fe1.getElement(), new HashSet<>()).contains(fe2.getElement()) &&
			goodDayStemMatch.getOrDefault(fe1.getReading(), new HashSet<>()).contains(fe2.getReading())) {
			score += BAD_RELATIONSHIP_WITH_GOOD_DAY_STEM_BONUS;
		}

		return score;
	}
}
