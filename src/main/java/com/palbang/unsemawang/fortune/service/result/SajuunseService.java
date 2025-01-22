package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SajuunseService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public SajuunseService(RestTemplate restTemplate, @Value("${external.api.sajuunse.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getSajuunseDetail(FortuneApiRequest request, String key) {
		ExternalSajuunseResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// key 검증 및 데이터 반환
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	// 외부 API 호출
	private ExternalSajuunseResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalSajuunseResponse.class);
	}

	// 응답 데이터를 Map<String, CommonResponse>로 처리
	private Map<String, CommonResponse> processApiResponse(ExternalSajuunseResponse apiResponse) {
		ExternalSajuunseResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("bornseasonluck", buildSimpleResponse("태어난 계절에 따른 운", result.getBornSeasonLuck()));
		responseMap.put("luck", buildLuck(result.getLuck()));
		responseMap.put("naturecharacter", buildSimpleResponse("타고난 성품", result.getNatureCharacter()));
		responseMap.put("socialcharacter", buildSimpleResponse("사회적 특성", result.getSocialCharacter()));
		responseMap.put("socialpersonality", buildSimpleResponse("사회적 성격", result.getSocialPersonality()));
		responseMap.put("avoidpeople", buildSimpleResponse("피해야 할 상대", result.getAvoidPeople()));
		responseMap.put("currentluckAnalysis",
			buildSimpleResponse("현재 나의 운 분석", result.getCurrentLuckAnalysis().getText()));

		return responseMap;
	}

	// 행운 관련 데이터 처리
	private CommonResponse buildLuck(ExternalSajuunseResponse.Result.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new CommonResponse(
			"행운", "",
			List.of(
				new CommonResponse("혈연", externalLuck.getBloodRelative(), null),
				new CommonResponse("직업", externalLuck.getJob(), null),
				new CommonResponse("성격", externalLuck.getPersonality(), null),
				new CommonResponse("애정운", externalLuck.getAffection(), null),
				new CommonResponse("건강운", externalLuck.getHealth(), null),
				new CommonResponse("길운 성씨", externalLuck.getGoodLuckFamilyName(), null)
			)
		);
	}

	// 심플한 CommonResponse 빌더
	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}
