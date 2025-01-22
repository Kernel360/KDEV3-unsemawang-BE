package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalUnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnsepuriService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public UnsepuriService(RestTemplate restTemplate, @Value("${external.api.unsepuri.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getUnsepuriDetail(FortuneApiRequest request, String key) {
		ExternalUnsepuriResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// 특정 key 필터링
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	// 외부 API 호출 메서드
	private ExternalUnsepuriResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalUnsepuriResponse.class);
	}

	// 응답 데이터를 Map<String, CommonResponse>로 변환
	private Map<String, CommonResponse> processApiResponse(ExternalUnsepuriResponse apiResponse) {
		ExternalUnsepuriResponse.Result result = apiResponse.getResult();

		// 데이터 매핑
		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("avoidpeople", buildSimpleResponse("피해야 할 상대", result.getAvoidPeople()));
		responseMap.put("currentunsepuri", buildSimpleResponse("현재 운세 풀이", result.getCurrentUnsepuri().getText()));
		responseMap.put("luckelement", buildSimpleResponse("행운의 요소", result.getLuckElement().getText()));

		return responseMap;
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null) {
			return null;
		}
		return new CommonResponse(label, value, null);
	}
}
