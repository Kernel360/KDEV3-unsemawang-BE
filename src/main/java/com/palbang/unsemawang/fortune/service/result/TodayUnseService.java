package com.palbang.unsemawang.fortune.service.result;

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

	private final RestTemplate restTemplate; // 인증서 무시 설정이 적용된 RestTemplate
	private final String apiUrl;

	public TodayUnseService(RestTemplate restTemplate, @Value("${external.api.todayunse.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public TodayUnseResponse getTodayUnseResult(FortuneApiRequest request) {
		ExternalTodayUnseResponse apiResponse = callExternalApi(request);
		// 데이터를 TodayUnseResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출 메서드
	private ExternalTodayUnseResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalTodayUnseResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalTodayUnseResponse.class);

			// 응답 데이터 로그
			log.info("Received response from external API: {}", response);

			return response;

		} catch (Exception e) {
			// 예외 발생 시 상세 정보 로깅
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	// 외부 API 응답 데이터를 처리하여 TodayUnseResponse로 변환
	private TodayUnseResponse processApiResponse(ExternalTodayUnseResponse apiResponse) {
		ExternalTodayUnseResponse.Result result = apiResponse.getResult();

		return new TodayUnseResponse(
			buildLuck(result.getLuck()) // 운세 (Luck) 데이터 처리
		);
	}

	// Luck 데이터 변환 메서드
	private TodayUnseResponse.Luck buildLuck(ExternalTodayUnseResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new TodayUnseResponse.Luck(
			new TodayUnseResponse.Luck.Total("전체 운세", externalLuck.getTotal()), // 총 운세
			new TodayUnseResponse.Luck.Love("연애운", externalLuck.getLove()), // 연애운
			new TodayUnseResponse.Luck.Hope("소망운", externalLuck.getHope()), // 소망운
			new TodayUnseResponse.Luck.Business("사업운", externalLuck.getBusiness()), // 사업운
			new TodayUnseResponse.Luck.Direction("방향 운세", externalLuck.getDirection()), // 방향 운세
			new TodayUnseResponse.Luck.Money("재물운", externalLuck.getMoney()) // 재물운
		);
	}
}