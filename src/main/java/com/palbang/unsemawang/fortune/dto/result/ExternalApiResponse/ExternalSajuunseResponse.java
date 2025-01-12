package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalSajuunseResponse {
	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private RequestInformation requestInformation;
		private String solvingTime;
		private String detailYear;
		private List<FortuneCommonResponse.Sajumyeongsik> sajumyeongsik;
		private String bornSeasonLuck;
		private Luck luck;
		private String natureCharacter;
		private String socialCharacter;
		private String socialPersonality;
		private String avoidPeople;
		private CurrentLuckAnalysis currentLuckAnalysis;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class RequestInformation {
			private String name;
			private String sex;
			private String solarDate;
			private String lunarDate;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Luck {
			private String bloodRelative;
			private String job;
			private String personality;
			private String affection;
			private String health;
			private String goodLuckFamilyName;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class CurrentLuckAnalysis {
			private String text;
			private int value;
		}
	}
}