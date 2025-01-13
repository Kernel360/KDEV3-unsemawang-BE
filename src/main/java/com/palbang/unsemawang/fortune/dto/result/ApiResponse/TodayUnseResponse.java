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
	@Schema(required = true)
	private Luck luck;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		@Schema(required = true)
		private Total total;
		@Schema(required = true)
		private Love love;
		@Schema(required = true)
		private Hope hope;
		@Schema(required = true)
		private Business business;
		@Schema(required = true)
		private Direction direction;
		@Schema(required = true)
		private Money money;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Total {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Love {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Hope {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Business {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Direction {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Money {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}
	}
}