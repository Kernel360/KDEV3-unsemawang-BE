package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalTojeongResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

@Service
public class TojeongService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public TojeongService(RestTemplate restTemplate, @Value("${external.api.tojeong.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getTojeongDetailByKey(FortuneApiRequest request, String key) {
		ExternalTojeongResponse apiResponse = callExternalApi(request); // 외부 API 호출
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// 특정 key 필터링
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key); // key에 해당하는 CommonResponse 반환
	}

	// 전체 Field -> Map<String, CommonResponse> 변환
	private Map<String, CommonResponse> processApiResponse(ExternalTojeongResponse apiResponse) {
		ExternalTojeongResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("currentluckanalysis", buildCurrentLuckAnalysis(result.getCurrentLuckAnalysis()));
		responseMap.put("thisyearluck", buildThisYearLuck(result.getThisYearLuck()));
		responseMap.put("tojeongsecret", buildTojeongSecret(result.getTojeongSecret()));
		responseMap.put("wealth", buildWealth(result.getWealth()));
		responseMap.put("naturecharacter", buildSimpleResponse("타고난 성품", result.getNatureCharacter()));
		responseMap.put("currentbehavior", buildSimpleResponse("현재 지켜야 할 처세", result.getCurrentBehavior()));
		responseMap.put("currenthumanrelationship",
			buildSimpleResponse("현재 대인 관계", result.getCurrentHumanRelationship()));
		responseMap.put("avoidpeople", buildSimpleResponse("피해야 할 상대", result.getAvoidPeople()));

		return responseMap; // 전체 Map 반환
	}

	private ExternalTojeongResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalTojeongResponse.class);
	}

	private CommonResponse buildCurrentLuckAnalysis(ExternalTojeongResponse.CurrentLuckAnalysis external) {
		if (external == null)
			return null;
		return new CommonResponse("현재 나의 운 분석", external.getText(), null);
	}

	private CommonResponse buildThisYearLuck(ExternalTojeongResponse.TojeongThisYearLuck external) {
		if (external == null)
			return null;

		List<CommonResponse> monthlyLuck = external.getMonth().stream()
			.map(month -> new CommonResponse(month.getMonth(), month.getValue(), null))
			.toList();

		List<CommonResponse> children = List.of(
			new CommonResponse("연애운", external.getRomanticRelationship(), null),
			new CommonResponse("건강운", external.getHealth(), null),
			new CommonResponse("직장운", external.getCompany(), null),
			new CommonResponse("소망운", external.getHope(), null),
			new CommonResponse("월별 운세", "", monthlyLuck)
		);

		return new CommonResponse("올해의 운세", "", children);
	}

	private CommonResponse buildTojeongSecret(ExternalTojeongResponse.TojeongSecret external) {
		if (external == null)
			return null;

		return new CommonResponse(
			"토정비결",
			"",
			List.of(
				new CommonResponse("일년신수(전반기)", external.getFirstHalf(), null),
				new CommonResponse("일년신수(후반기)", external.getSecondHalf(), null)
			)
		);
	}

	private CommonResponse buildWealth(ExternalTojeongResponse.Wealth external) {
		if (external == null)
			return null;

		return new CommonResponse(
			"재물 운세",
			"",
			List.of(
				new CommonResponse("재물 운의 특성", external.getCharacteristics(), null),
				new CommonResponse("재물 모으는 법", external.getAccumulate(), null),
				new CommonResponse("재물 손실 방지", external.getPrevent(), null),
				new CommonResponse("재테크", external.getInvestment(), null),
				new CommonResponse("현재 재물운", external.getCurrentLuck(), null)
			)
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}