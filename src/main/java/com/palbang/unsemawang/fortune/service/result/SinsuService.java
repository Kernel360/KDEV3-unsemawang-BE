package com.palbang.unsemawang.fortune.service.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SinsuService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public SinsuService(RestTemplate restTemplate, @Value("${external.api.sinsu.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 특정 key의 CommonResponse 반환
	public CommonResponse getSinsuDetail(FortuneApiRequest request, String key) {
		ExternalSinsuResponse apiResponse = callExternalApi(request);
		Map<String, CommonResponse> responseMap = processApiResponse(apiResponse);

		// key 검증 및 데이터 반환
		if (!responseMap.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key: " + key);
		}

		return responseMap.get(key);
	}

	// 외부 API 호출 메서드
	private ExternalSinsuResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalSinsuResponse.class);
	}

	// 응답 데이터를 Map<String, CommonResponse>로 처리
	private Map<String, CommonResponse> processApiResponse(ExternalSinsuResponse apiResponse) {
		ExternalSinsuResponse.Result result = apiResponse.getResult();

		Map<String, CommonResponse> responseMap = new HashMap<>();
		responseMap.put("thisyearluck", buildThisYearLuck(result.getThisYearLuck()));
		responseMap.put("goodandbadanalysis", buildGoodAndBadAnalysis(result.getGoodAndBadAnalysis()));
		return responseMap;
	}

	// 올해의 운세 데이터를 CommonResponse로 가공 처리
	private CommonResponse buildThisYearLuck(ExternalSinsuResponse.ThisYearLuck externalThisYearLuck) {
		if (externalThisYearLuck == null) {
			return null;
		}

		// 월별 운세 처리
		List<CommonResponse> monthlyLuck = externalThisYearLuck.getMonth()
			.stream()
			.map(monthLuck -> new CommonResponse(monthLuck.getMonth(), monthLuck.getValue(), null))
			.toList();

		// 하위 운세(children) 처리
		List<CommonResponse> children = List.of(
			new CommonResponse("총운", externalThisYearLuck.getTotal(), null),
			new CommonResponse("사업운", externalThisYearLuck.getBusiness(), null),
			new CommonResponse("연애운", externalThisYearLuck.getLove(), null),
			new CommonResponse("건강운", externalThisYearLuck.getHealth(), null),
			new CommonResponse("이동운", externalThisYearLuck.getTravelMoving(), null),
			new CommonResponse("직업운", externalThisYearLuck.getWork(), null),
			new CommonResponse("월별 운세", "", monthlyLuck) // 월별 운세 추가
		);

		// 상위 CommonResponse 반환
		return new CommonResponse("올해의 행운", "", children);
	}

	// 길흉 분석 추가
	private CommonResponse buildGoodAndBadAnalysis(
		ExternalSinsuResponse.GoodAndBadAnalysis externalGoodAndBadAnalysis) {
		if (externalGoodAndBadAnalysis == null) {
			return null;
		}

		String currentGoodOrBadText = externalGoodAndBadAnalysis.getCurrentGoodOrBad().getText();

		// 하위 운세(children) 처리
		List<CommonResponse> children = List.of(
			new CommonResponse("대길", externalGoodAndBadAnalysis.getGood(), null),
			new CommonResponse("대흉", externalGoodAndBadAnalysis.getBad(), null),
			new CommonResponse("직업에 따른 길흉", externalGoodAndBadAnalysis.getGoodOrBadByJob(), null),
			new CommonResponse("명당과 길흉", externalGoodAndBadAnalysis.getPlaceGoodOrBad(), null),
			new CommonResponse("현재의 길흉사", currentGoodOrBadText, null)
		);

		// 상위 CommonResponse 반환
		return new CommonResponse("길흉 분석", "", children);
	}
}
