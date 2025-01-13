package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사주 운세 Response DTO")
public class SajuunseResponse {
	// 태어난 계절에 따른 운
	@Schema(required = true)
	private BornSeasonLuck bornSeasonLuck;

	// 행운
	@Schema(required = true)
	private Luck luck;

	// 타고난 성품
	@Schema(required = true)
	private NatureCharacter natureCharacter;

	// 사회적 특성
	@Schema(required = true)
	private SocialCharacter socialCharacter;

	// 사회적 성격
	@Schema(required = true)
	private SocialPersonality socialPersonality;

	// 피해야 할 상대
	@Schema(required = true)
	private AvoidPeople avoidPeople;

	// 현재 나의 운 분석
	@Schema(required = true)
	private CurrentLuckAnalysis currentLuckAnalysis;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BornSeasonLuck {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Children {
			@Schema(required = true)
			private BloodRelative bloodRelative;
			@Schema(required = true)
			private Job job;
			@Schema(required = true)
			private Personality personality;
			@Schema(required = true)
			private Affection affection;
			@Schema(required = true)
			private Health health;
			@Schema(required = true)
			private GoodLuckFamilyName goodLuckFamilyName;

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class BloodRelative {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Job {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Personality {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Affection {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Health {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class GoodLuckFamilyName {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class NatureCharacter {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialCharacter {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialPersonality {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

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
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CurrentLuckAnalysis {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
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