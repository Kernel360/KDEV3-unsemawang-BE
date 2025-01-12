package com.palbang.unsemawang.fortune.service.result;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ChildMap {

	// 공통적인 Child 생성 (value, children이 없는 경우)
	public static Map<String, Object> createChildMap(String key, String label) {
		return Map.of(
			key, Map.of(
				"label", label
			)
		);
	}

	// 공통적인 Child 생성 (children이 없는 경우)
	public static Map<String, Object> createChildMap(String key, String label, String value) {
		if (value == null) {
			value = "N/A"; // null 값 기본 처리
		}
		return Map.of(
			key, Map.of(
				"label", label,
				"value", value
			)
		);
	}

	// 공통적인 Child 생성 (children이 있는 경우)
	public static Map<String, Object> createChildMap(String key, String label, String value,
		List<Map<String, Object>> children) {
		if (value == null) {
			return Map.of(
				key, Map.of(
					"label", label,
					"children", children
				)
			);
		}
		return Map.of(
			key, Map.of(
				"label", label,
				"value", value,
				"children", children
			)
		);
	}

	// 공통적인 Child 생성 (value가 없고 children이 있는 경우)
	public static Map<String, Object> createChildMap(String key, String label,
		List<Map<String, Object>> children) {
		return Map.of(
			key, Map.of(
				"label", label,
				"children", children
			)
		);
	}

	// 공통 텍스트 데이터 처리
	public static Optional<Map<String, Object>> processSimpleText(String label, String value) {
		if (value != null && !value.isEmpty()) {
			// children이 없는 단순 텍스트 데이터를 반환
			return Optional.of(Map.of(
				"label", label,
				"value", value
			));
		}
		return Optional.empty();
	}
}