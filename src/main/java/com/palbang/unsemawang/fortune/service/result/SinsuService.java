package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SinsuResponse;
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

	// 신수 결과 가져오기
	public SinsuResponse getSinsuResult(FortuneApiRequest request) {
		ExternalSinsuResponse apiResponse = callExternalApi(request);
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

	// API 응답 데이터를 처리
	private SinsuResponse processApiResponse(ExternalSinsuResponse apiResponse) {
		ExternalSinsuResponse.Result result = apiResponse.getResult();
		return new SinsuResponse(buildThisYearLuck(result.getThisYearLuck()));
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
}