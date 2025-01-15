package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.TodayUnseResponse;
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

	public TodayUnseResponse getTodayUnseResult(FortuneApiRequest request) {
		ExternalTodayUnseResponse apiResponse = callExternalApi(request);
		return processApiResponse(apiResponse);
	}

	private ExternalTodayUnseResponse callExternalApi(FortuneApiRequest request) {
		ExternalTodayUnseResponse response = restTemplate.postForObject(apiUrl, request,
			ExternalTodayUnseResponse.class);

		return response;
	}

	private TodayUnseResponse processApiResponse(ExternalTodayUnseResponse apiResponse) {
		ExternalTodayUnseResponse.Result result = apiResponse.getResult();

		return new TodayUnseResponse(
			buildLuck(result.getLuck()) // `Luck` 데이터를 가공
		);
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
