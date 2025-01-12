package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "오늘의 운세 Response DTO")
public class TodayUnseResponse {

	// 오행 분석
	private Luck luck;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		private Total total;
		private Love love;
		private Hope hope;
		private Business business;
		private Direction direction;
		private Money money;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Total {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Love {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Hope {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Business {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Direction {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Money {
			private String label;
			private String value;
		}
	}
}