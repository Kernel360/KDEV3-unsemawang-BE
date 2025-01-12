package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalTojeongResponse {
	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private String detailYear;
		private String solvingTime;
		private String natureCharacter;
		private String currentBehavior;
		private String currentHumanRelationship;
		private CurrentLuckAnalysis currentLuckAnalysis;
		private String avoidPeople;
		private String goodLuckFamilyName;
		private FortuneCommonResponse.OhaengAnalysis ohaengAnalysis;
		private List<FortuneCommonResponse.Sajumyeongsik> sajumyeongsik;
		private TojeongSecret tojeongSecret;
		private TojeongThisYearLuck thisYearLuck;
		private Wealth wealth;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentLuckAnalysis {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TojeongSecret {
		private String firstHalf;
		private String secondHalf;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TojeongThisYearLuck {
		private String romanticRelationship;
		private String health;
		private String company;
		private String hope;
		private List<MonthLuck> month;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class MonthLuck {
			private String month;
			private String value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Wealth {
		private String characteristics;
		private String accumulate;
		private String prevent;
		private String investment;
		private String currentLuck;
	}
}