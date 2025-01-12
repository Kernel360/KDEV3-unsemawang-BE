package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalInsaengResponse {
	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private RequestInformation requestInformation;
		private String solvingTime;
		private String detailYear;
		private List<FortuneCommonResponse.Sajumyeongsik> sajumyeongsik;
		private FortuneCommonResponse.OhaengAnalysis ohaengAnalysis;
		private Total total;
		private Gunghap gunghap;
		private MarryGung marryGung;
		private Luck luck;
		private String eightStar;
		private String constellation;
		private SoulMates soulMates;
		private CurrentGoodAndBadNews currentGoodAndBadNews;
		private String goodAndBadByPungsu;
	}

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
	public static class Total {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Gunghap {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MarryGung {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Luck {
		private EarlyYears earlyYears;
		private MiddleYears middleYears;
		private LaterYears laterYears;
		private String personality;
		private String aptitude;
		private String job;
		private Health health;
		private String housing;
		private Love love;
		private Sex sex;
		private Money money;
		private String luck;
		private String currentColor;
		private String familyName;
		private String place;
		private FitNumber fitNumber;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class EarlyYears {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class MiddleYears {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class LaterYears {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Health {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Love {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Sex {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Money {
			private String text;
			private int value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class FitNumber {
			private String text;
			private int value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SoulMates {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentGoodAndBadNews {
		private String text;
		private int value;
	}
}