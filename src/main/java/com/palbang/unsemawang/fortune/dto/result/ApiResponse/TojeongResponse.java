package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "토정비결 Response DTO Test")
public class TojeongResponse {
	// 현재 나의 운 분석
	@Schema(required = true)
	private CurrentLuckAnalysis currentLuckAnalysis;

	// 올해의 운세
	@Schema(required = true)
	private ThisYearLuck thisYearLuck;

	// 토정비결
	@Schema(required = true)
	private TojeongSecret tojeongSecret;

	// 재물
	@Schema(required = true)
	private Wealth wealth;

	// 타고난 성품
	@Schema(required = true)
	private NatureCharacter natureCharacter;

	// 현재 지켜야 할 처세
	@Schema(required = true)
	private CurrentBehavior currentBehavior;

	// 현재 대인 관계
	@Schema(required = true)
	private CurrentHumanRelationship currentHumanRelationship;

	// 피해야 할 상대
	@Schema(required = true)
	private AvoidPeople avoidPeople;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentLuckAnalysis {
		@Schema(required = true)
		private String label;
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

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ThisYearLuck {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private RomanticRelationship romanticRelationship;
			@Schema(required = true)
			private Health health;
			@Schema(required = true)
			private Company company;
			@Schema(required = true)
			private Hope hope;
			@Schema(required = true)
			private MonthlyLuck monthlyLuck;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class RomanticRelationship {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Health {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Company {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Hope {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class MonthlyLuck {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private List<Month> month;

				@Data
				@NoArgsConstructor
				@AllArgsConstructor
				public static class Month {
					@Schema(required = true)
					private String label;
					@Schema(required = true)
					private String value;
				}
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TojeongSecret {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private FirstHalf firstHalf;
			@Schema(required = true)
			private SecondHalf secondHalf;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class FirstHalf {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class SecondHalf {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Wealth {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Characteristics characteristics;
			@Schema(required = true)
			private Accumulate accumulate;
			@Schema(required = true)
			private Prevent prevent;
			@Schema(required = true)
			private Invesetment invesetment;
			@Schema(required = true)
			private CurrentLuck currentLuck;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Characteristics {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Accumulate {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Prevent {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Invesetment {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class CurrentLuck {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NatureCharacter {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentBehavior {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentHumanRelationship {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AvoidPeople {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}
}