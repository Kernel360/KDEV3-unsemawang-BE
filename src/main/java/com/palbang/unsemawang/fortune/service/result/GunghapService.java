package com.palbang.unsemawang.fortune.service.result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.GunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalGunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.GunghapApiRequest;

@Service
public class GunghapService {

	private final RestTemplate restTemplate = new RestTemplate(); // REST API 호출용
	private final String apiUrl;

	public GunghapService(@Value("${external.api.gunghap.url}") String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public GunghapResponse getGunghapResult(GunghapApiRequest request) {
		ExternalGunghapResponse apiResponse = callExternalApi(request);
		// 데이터를 GunghapResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	private ExternalGunghapResponse callExternalApi(GunghapApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalGunghapResponse.class);
	}

	private GunghapResponse processApiResponse(ExternalGunghapResponse apiResponse) {
		ExternalGunghapResponse.Result result = apiResponse.getResult();

		return new GunghapResponse(
			buildInnerGunghap(result.getInnerGunghap()), // 속궁합
			buildManFemaleFate(result.getManFemaleFate()), // 남녀 운명
			buildOuterGunghap(result.getOuterGunghap()), // 겉궁합
			buildTypeAnalysis(result.getTypeAnalysis()) // 타입 분석
		);
	}

	// 속궁합 처리
	private GunghapResponse.InnerGunghap buildInnerGunghap(String innerGunghap) {
		return new GunghapResponse.InnerGunghap("속궁합", innerGunghap);
	}

	// 남녀 운명 처리
	private GunghapResponse.ManFemaleFate buildManFemaleFate(String manFemaleFate) {
		return new GunghapResponse.ManFemaleFate("남녀 운명", manFemaleFate);
	}

	// 겉궁합 처리
	private GunghapResponse.OuterGunghap buildOuterGunghap(String outerGunghap) {
		return new GunghapResponse.OuterGunghap("겉궁합", outerGunghap);
	}

	// 타입 분석 처리
	private GunghapResponse.TypeAnalysis buildTypeAnalysis(String typeAnalysis) {
		return new GunghapResponse.TypeAnalysis("타입 분석", typeAnalysis);
	}
}