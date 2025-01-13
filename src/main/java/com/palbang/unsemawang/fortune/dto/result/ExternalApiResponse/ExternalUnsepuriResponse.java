package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalUnsepuriResponse {

	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private String solvingTime;
		private String detailYear;
		private CurrentUnsepuri currentUnsepuri;
		private String avoidPeople;
		private LuckElement luckElement;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentUnsepuri {
		private String text;
		private int value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LuckElement {
		private String text;
		private int value;
	}
}