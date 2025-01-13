package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTodayUnseResponse {

	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private String solvingTime;
		private String detailYear;
		private List<FortuneCommonResponse.Sajumyeongsik> sajumyeongsik;
		private FortuneCommonResponse.OhaengAnalysis ohaengAnalysis; //
		private Luck luck;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Luck {
		private String total;
		private String love;
		private String hope;
		private String business;
		private String direction;
		private String money;
	}
}