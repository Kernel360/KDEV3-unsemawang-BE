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
	private BornSeasonLuck bornSeasonLuck;

	// 행운
	private Luck luck;

	// 타고난 성품
	private NatureCharacter natureCharacter;

	// 사회적 특성
	private SocialCharacter socialCharacter;

	// 사회적 성격
	private SocialPersonality socialPersonality;

	// 피해야 할 상대
	private AvoidPeople avoidPeople;

	// 현재 나의 운 분석
	private CurrentLuckAnalysis currentLuckAnalysis;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class BornSeasonLuck {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		private String label;
		private List<Children> children;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Children {
			private BloodRelative bloodRelative;
			private Job job;
			private Personality personality;
			private Affection affection;
			private Health health;
			private GoodLuckFamilyName goodLuckFamilyName;

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class BloodRelative {
				private String label;
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Job {
				private String label;
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Personality {
				private String label;
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Affection {
				private String label;
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Health {
				private String label;
				private String value;
			}

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class GoodLuckFamilyName {
				private String label;
				private String value;
			}
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class NatureCharacter {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialCharacter {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialPersonality {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AvoidPeople {
		private String label;
		private String value;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CurrentLuckAnalysis {
		private String label;
		private List<Children> children;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Children {
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
}