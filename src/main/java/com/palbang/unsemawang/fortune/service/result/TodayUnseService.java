package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
		try {
			log.info("Requesting external API with data: {}", request);
			ExternalTodayUnseResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalTodayUnseResponse.class);
			log.info("Received response from external API: {}", response);
			return response;
		} catch (Exception e) {
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	private TodayUnseResponse processApiResponse(ExternalTodayUnseResponse apiResponse) {
		ExternalTodayUnseResponse.Result result = apiResponse.getResult();

		return new TodayUnseResponse(
			buildLuck(result.getLuck()) // `Luck` 데이터를 가공
		);
	}

	private TodayUnseResponse.Luck buildLuck(ExternalTodayUnseResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		// `Children` 객체 생성
		List<TodayUnseResponse.Luck.Children> children = List.of(
			new TodayUnseResponse.Luck.Children(
				new TodayUnseResponse.Luck.Children.Total("전체 운세", externalLuck.getTotal()),
				new TodayUnseResponse.Luck.Children.Love("연애운", externalLuck.getLove()),
				new TodayUnseResponse.Luck.Children.Hope("소망운", externalLuck.getHope()),
				new TodayUnseResponse.Luck.Children.Business("사업운", externalLuck.getBusiness()),
				new TodayUnseResponse.Luck.Children.Direction("방향 운세", externalLuck.getDirection()),
				new TodayUnseResponse.Luck.Children.Money("재물운", externalLuck.getMoney())
			)
		);

		// `Luck` 생성
		return new TodayUnseResponse.Luck(
			"오늘의 운세", // label 값 설정
			children
		);
	}
}