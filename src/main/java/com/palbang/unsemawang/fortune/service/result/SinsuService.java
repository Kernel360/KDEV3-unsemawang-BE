package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SinsuService {

	private final RestTemplate restTemplate; // 인증서 무시 설정이 적용된 RestTemplate
	private final String apiUrl;

	public SinsuService(RestTemplate restTemplate, @Value("${external.api.sinsu.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// 신수 결과 가져오기
	public SinsuResponse getSinsuResult(FortuneApiRequest request) {
		ExternalSinsuResponse apiResponse = callExternalApi(request);
		// 데이터를 SinsuResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출 메서드
	private ExternalSinsuResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalSinsuResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalSinsuResponse.class);

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

	// API 응답 데이터를 처리하여 SinsuResponse로 변환
	private SinsuResponse processApiResponse(ExternalSinsuResponse apiResponse) {
		ExternalSinsuResponse.Result result = apiResponse.getResult();

		// ThisYearLuck 객체 생성
		return new SinsuResponse(buildThisYearLuck(result.getThisYearLuck()));
	}

	// ThisYearLuck 데이터 처리 메서드
	private SinsuResponse.ThisYearLuck buildThisYearLuck(ExternalSinsuResponse.ThisYearLuck external) {
		if (external == null)
			return null;

		// 월별 운세 데이터 생성
		List<SinsuResponse.ThisYearLuck.MonthlyLuck.Month> months = external.getMonth().stream()
			.map(month -> new SinsuResponse.ThisYearLuck.MonthlyLuck.Month(month.getMonth(), month.getValue()))
			.toList();

		// MonthlyLuck 객체 생성
		SinsuResponse.ThisYearLuck.MonthlyLuck monthlyLuck = new SinsuResponse.ThisYearLuck.MonthlyLuck(
			"월별 운세",
			months
		);

		// ThisYearLuck 객체 반환
		return new SinsuResponse.ThisYearLuck(
			new SinsuResponse.ThisYearLuck.Total("전체 운세", external.getTotal()),
			new SinsuResponse.ThisYearLuck.Business("사업운", external.getBusiness()),
			new SinsuResponse.ThisYearLuck.Love("연애운", external.getLove()),
			new SinsuResponse.ThisYearLuck.Health("건강운", external.getHealth()),
			new SinsuResponse.ThisYearLuck.TravelMoving("이동운", external.getTravelMoving()),
			new SinsuResponse.ThisYearLuck.Work("직업운", external.getWork()),
			monthlyLuck
		);
	}
}