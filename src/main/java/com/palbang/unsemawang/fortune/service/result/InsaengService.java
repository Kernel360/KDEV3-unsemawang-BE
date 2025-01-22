package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalInsaengResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InsaengService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public InsaengService(RestTemplate restTemplate, @Value("${external.api.insaeng.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getInsaengDetail(FortuneApiRequest request, String key) {
		ExternalInsaengResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// key 검증 및 데이터 반환
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	private ExternalInsaengResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalInsaengResponse.class);
	}

	// API 응답 전체를 Map<String, CommonResponse>로 처리
	private Map<String, CommonResponse> processApiResponse(ExternalInsaengResponse apiResponse) {
		ExternalInsaengResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("total", buildSimpleResponse("총평", result.getTotal().getText()));
		responseMap.put("gunghap", buildSimpleResponse("궁합", result.getGunghap().getText()));
		responseMap.put("marrygung", buildSimpleResponse("결혼 관련 정보", result.getMarryGung().getText()));
		responseMap.put("luck", buildLuck(result.getLuck()));
		responseMap.put("constellation", buildSimpleResponse("별자리", result.getConstellation()));
		responseMap.put("currentgoodandbad",
			buildSimpleResponse("현재의 길흉사", result.getCurrentGoodAndBadNews().getText()));
		responseMap.put("eightstar", buildSimpleResponse("팔복궁", result.getEightStar()));
		responseMap.put("pungsu", buildSimpleResponse("풍수로 보는 길흉", result.getGoodAndBadByPungsu()));
		responseMap.put("soulmates", buildSimpleResponse("천생연분", result.getSoulMates().getText()));

		return responseMap;
	}

	private CommonResponse buildLuck(ExternalInsaengResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new CommonResponse(
			"행운", "",
			List.of(
				new CommonResponse("성격", externalLuck.getPersonality(), null),
				new CommonResponse("직업운", externalLuck.getJob(), null),
				new CommonResponse("건강운", externalLuck.getHealth().getText(), null)
			)
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}
