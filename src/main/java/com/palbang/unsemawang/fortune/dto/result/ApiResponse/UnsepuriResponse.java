package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Schema(description = "현재 운세 풀이 Response DTO")
public class UnsepuriResponse {

	// 피해야 할 상대
	private AvoidPeople avoidPeople;

	// 현재 운세 풀이
	private CurrentUnsepuri currentUnsepuri;

	// 행운의 요소
	private LuckElement luckElement;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AvoidPeople {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentUnsepuri {
		private String label;
		private Text text;
		private Value value;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Text {
			private String label;
			private String value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Value {
			private String label;
			private int value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LuckElement {
		private String label;
		private Text text;
		private Value value;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Text {
			private String label;
			private String value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Value {
			private String label;
			private int value;
		}
	}
}