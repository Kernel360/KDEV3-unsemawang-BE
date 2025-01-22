package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalTodayUnseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodayUnseService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public TodayUnseService(RestTemplate restTemplate, @Value("${external.api.todayunse.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getTodayUnseDetail(FortuneApiRequest request, String key) {
		ExternalTodayUnseResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// key 검증 및 데이터 반환
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	// 외부 API 호출 메서드
	private ExternalTodayUnseResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalTodayUnseResponse.class);
	}

	// 응답 데이터를 Map<String, CommonResponse>로 처리
	private Map<String, CommonResponse> processApiResponse(ExternalTodayUnseResponse apiResponse) {
		ExternalTodayUnseResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("luck", buildLuck(result.getLuck()));
		return responseMap;
	}

	private CommonResponse buildLuck(ExternalTodayUnseResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		// `Children` 객체 생성
		List<CommonResponse> children = List.of(
			new CommonResponse("전체 운세", externalLuck.getTotal(), null),
			new CommonResponse("연애운", externalLuck.getLove(), null),
			new CommonResponse("소망운", externalLuck.getHope(), null),
			new CommonResponse("사업운", externalLuck.getBusiness(), null),
			new CommonResponse("방향 운세", externalLuck.getDirection(), null),
			new CommonResponse("재물운", externalLuck.getMoney(), null)
		);

		// `Luck` 생성
		return new CommonResponse("오늘의 운세", "", children);
	}
}
