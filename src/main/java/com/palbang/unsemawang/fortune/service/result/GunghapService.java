package com.palbang.unsemawang.fortune.service.result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.GunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalGunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.GunghapApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GunghapService {

	private final RestTemplate restTemplate; // 인증서 무시 설정이 적용된 RestTemplate
	private final String apiUrl;

	public GunghapService(RestTemplate restTemplate, @Value("${external.api.gunghap.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public GunghapResponse getGunghapResult(GunghapApiRequest request) {
		ExternalGunghapResponse apiResponse = callExternalApi(request);
		// 데이터를 GunghapResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	private ExternalGunghapResponse callExternalApi(GunghapApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalGunghapResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalGunghapResponse.class);

			// 응답 데이터 로그
			log.info("Received response from external API: {}", response);

			return response;

		} catch (Exception e) {
			// 예외 발생 시 상세 정보 로깅
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	private GunghapResponse processApiResponse(ExternalGunghapResponse apiResponse) {
		ExternalGunghapResponse.Result result = apiResponse.getResult();

		return new GunghapResponse(
			buildSimpleResponse("속궁합", result.getInnerGunghap()), // 속궁합
			buildSimpleResponse("남녀 운명", result.getManFemaleFate()), // 남녀 운명
			buildSimpleResponse("겉궁합", result.getOuterGunghap()), // 겉궁합
			buildSimpleResponse("타입 분석", result.getTypeAnalysis()) // 타입 분석
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}