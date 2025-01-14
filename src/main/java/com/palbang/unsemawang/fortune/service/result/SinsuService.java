package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSinsuResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
			log.info("Requesting external API with data: {}", request);
			ExternalSinsuResponse response = restTemplate.postForObject(apiUrl, request, ExternalSinsuResponse.class);
			log.info("Received response from external API: {}", response);
			return response;
		} catch (Exception e) {
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	// API 응답 데이터를 처리하여 SinsuResponse로 변환
	private SinsuResponse processApiResponse(ExternalSinsuResponse apiResponse) {
		ExternalSinsuResponse.Result result = apiResponse.getResult();

		// ThisYearLuck 가공하여 반환
		return new SinsuResponse(
			buildThisYearLuck(result.getThisYearLuck()) // 올해의 행운 정보 조립
		);
	}

	// ThisYearLuck 데이터 처리 메서드
	// ThisYearLuck 데이터 처리 메서드
	private SinsuResponse.ThisYearLuck buildThisYearLuck(ExternalSinsuResponse.ThisYearLuck externalThisYearLuck) {
		if (externalThisYearLuck == null) {
			return null;
		}

		// 월별 운세 가공
		List<SinsuResponse.ThisYearLuck.Children.MonthlyLuck.Month> months = externalThisYearLuck.getMonth()
			.stream()
			.map(monthLuck -> new SinsuResponse.ThisYearLuck.Children.MonthlyLuck.Month(
				monthLuck.getMonth(), // ExternalSinsuResponse.MonthLuck의 month 필드 매핑
				monthLuck.getValue()  // ExternalSinsuResponse.MonthLuck의 value 필드 매핑
			))
			.toList();

		// MonthlyLuck 객체 생성
		SinsuResponse.ThisYearLuck.Children.MonthlyLuck monthlyLuck = new SinsuResponse.ThisYearLuck.Children.MonthlyLuck(
			"월별 운세", // label 설정
			months
		);

		// ThisYearLuck.Children 생성
		SinsuResponse.ThisYearLuck.Children children = new SinsuResponse.ThisYearLuck.Children(
			new SinsuResponse.ThisYearLuck.Children.Total("총운", externalThisYearLuck.getTotal()), // 총운
			new SinsuResponse.ThisYearLuck.Children.Business("사업운", externalThisYearLuck.getBusiness()), // 사업운
			new SinsuResponse.ThisYearLuck.Children.Love("연애운", externalThisYearLuck.getLove()), // 연애운
			new SinsuResponse.ThisYearLuck.Children.Health("건강운", externalThisYearLuck.getHealth()), // 건강운
			new SinsuResponse.ThisYearLuck.Children.TravelMoving("이동운", externalThisYearLuck.getTravelMoving()), // 이동운
			new SinsuResponse.ThisYearLuck.Children.Work("직업운", externalThisYearLuck.getWork()), // 직업운
			monthlyLuck // 월별 운세 추가
		);

		// ThisYearLuck 반환
		return new SinsuResponse.ThisYearLuck(
			"올해의 행운", // label
			List.of(children) // Children 리스트 추가
		);
	}
}