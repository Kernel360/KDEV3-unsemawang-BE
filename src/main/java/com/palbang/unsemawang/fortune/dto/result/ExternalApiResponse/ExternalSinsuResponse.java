package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalSinsuResponse {
	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private String solvingTime;
		private String detailYear;
		private List<FortuneCommonResponse.Sajumyeongsik> sajumyeongsik;
		private FortuneCommonResponse.OhaengAnalysis ohaengAnalysis;
		private String slogan;
		private ThisYearLuck thisYearLuck;
		private FortuneCommonResponse.Wealth wealth;
		private String timelyLuck;
		private GoodAndBadAnalysis goodAndBadAnalysis;
		private String numberLuck;
		private Luck luck;
		private String fate;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ThisYearLuck {
		private String total;
		private String business;
		private String love;
		private String health;
		private String travelMoving;
		private String work;
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
	public static class GoodAndBadAnalysis {
		private String good;
		private String bad;
		private String goodOrBadByJob;
		private String placeGoodOrBad;
		private CurrentGoodOrBad currentGoodOrBad;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class CurrentGoodOrBad {
			private String text;
			private int value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Luck {
		private String color;
		private String familyName;
		private String currentDirection;
		private String place;
		private FitNumber fitNumber;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class FitNumber {
			private String text;
			private int value;
		}
	}
}