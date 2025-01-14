package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "현재 운세 풀이 Response DTO")
public class UnsepuriResponse {

	// 피해야 할 상대
	@Schema(required = true)
	private AvoidPeople avoidPeople;

	// 현재 운세 풀이
	@Schema(required = true)
	private CurrentUnsepuri currentUnsepuri;

	// 행운의 요소
	@Schema(required = true)
	private LuckElement luckElement;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AvoidPeople {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentUnsepuri {
		@Schema(required = true)
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Text text;
			@Schema(required = true)
			private Value value;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Text {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Value {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private int value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LuckElement {
		@Schema(required = true)
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Text text;
			@Schema(required = true)
			private Value value;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Text {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Value {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private int value;
			}
		}
	}
}