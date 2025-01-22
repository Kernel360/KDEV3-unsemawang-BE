package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalGunghapResponse;
import com.palbang.unsemawang.fortune.dto.result.GunghapApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GunghapService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public GunghapService(RestTemplate restTemplate, @Value("${external.api.gunghap.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getGunghapDetail(GunghapApiRequest request, String key) {
		ExternalGunghapResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// key에 해당하는 데이터 반환
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	private ExternalGunghapResponse callExternalApi(GunghapApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalGunghapResponse.class);
	}

	// 전체 항목을 Map<String, CommonResponse>로 처리
	private Map<String, CommonResponse> processApiResponse(ExternalGunghapResponse apiResponse) {
		ExternalGunghapResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("innergunghap", buildSimpleResponse("속궁합", result.getInnerGunghap()));
		responseMap.put("manfemalefate", buildSimpleResponse("남녀 운명", result.getManFemaleFate()));
		responseMap.put("outergunghap", buildSimpleResponse("겉궁합", result.getOuterGunghap()));
		responseMap.put("typeanalysis", buildSimpleResponse("타입 분석", result.getTypeAnalysis()));

		return responseMap;
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}