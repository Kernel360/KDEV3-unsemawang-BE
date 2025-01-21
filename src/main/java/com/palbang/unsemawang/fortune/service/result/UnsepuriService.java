package com.palbang.unsemawang.fortune.service.result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.UnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalUnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UnsepuriService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public UnsepuriService(RestTemplate restTemplate, @Value("${external.api.unsepuri.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// Unsepuri 결과 반환
	public UnsepuriResponse getUnsepuriResult(FortuneApiRequest request) {
		ExternalUnsepuriResponse apiResponse = callExternalApi(request);
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출
	private ExternalUnsepuriResponse callExternalApi(FortuneApiRequest request) {
		ExternalUnsepuriResponse response = restTemplate.postForObject(apiUrl, request,
			ExternalUnsepuriResponse.class);

		return response;
	}

	// API 응답 데이터를 UnsepuriResponse로 변환
	private UnsepuriResponse processApiResponse(ExternalUnsepuriResponse apiResponse) {
		ExternalUnsepuriResponse.Result result = apiResponse.getResult();

		return new UnsepuriResponse(
			buildSimpleResponse("피해야 할 상대", result.getAvoidPeople()), // 피해야 할 상대
			buildSimpleResponse("현재 운세 풀이", result.getCurrentUnsepuri().getText()), // 현재 운세 풀이
			buildSimpleResponse("행운의 요소", result.getLuckElement().getText()) // 행운의 요소
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}
