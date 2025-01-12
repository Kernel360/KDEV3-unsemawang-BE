package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalGunghapResponse {

	private Result result;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result {
		private RequestInformation requestInformation;
		private RequestInformation otherRequestInformation;
		private String solvingTime;
		private String detailYear;
		private List<FortuneCommonResponse.Sajumyeongsik> meSajumyeongsik;
		private List<FortuneCommonResponse.Sajumyeongsik> otherSajumyeongsik;
		private FortuneCommonResponse.OhaengAnalysis ohaengAnalysis;
		private String innerGunghap;
		private String outerGunghap;
		private String manFemaleFate;
		private String typeAnalysis;
		private String datingTips;
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
}