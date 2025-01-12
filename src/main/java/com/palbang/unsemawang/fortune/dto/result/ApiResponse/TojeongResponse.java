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
	private CurrentLuckAnalysis currentLuckAnalysis;

	// 올해의 운세
	private ThisYearLuck thisYearLuck;

	// 토정비결
	private TojeongSecret tojeongSecret;

	// 재물
	private Wealth wealth;

	// 타고난 성품
	private NatureCharacter natureCharacter;

	// 현재 지켜야 할 처세
	private CurrentBehavior currentBehavior;

	// 현재 대인 관계
	private CurrentHumanRelationship currentHumanRelationship;

	// 피해야 할 상대
	private AvoidPeople avoidPeople;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentLuckAnalysis {
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
	public static class ThisYearLuck {
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			private RomanticRelationship romanticRelationship;
			private Health health;
			private Company company;
			private Hope hope;
			private MonthlyLuck monthlyLuck;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class RomanticRelationship {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Health {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Company {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Hope {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class MonthlyLuck {
				private String label;
				private List<Month> month;

				@Data
				@NoArgsConstructor
				@AllArgsConstructor
				public static class Month {
					private String label;
					private String value;
				}
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TojeongSecret {
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			private FirstHalf firstHalf;
			private SecondHalf secondHalf;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class FirstHalf {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class SecondHalf {
				private String label;
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Wealth {
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			private Characteristics characteristics;
			private Accumulate accumulate;
			private Prevent prevent;
			private Invesetment invesetment;
			private CurrentLuck currentLuck;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Characteristics {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Accumulate {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Prevent {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Invesetment {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class CurrentLuck {
				private String label;
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NatureCharacter {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentBehavior {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentHumanRelationship {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AvoidPeople {
		private String label;
		private String value;
	}
}