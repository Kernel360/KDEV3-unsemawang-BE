package com.palbang.unsemawang.fortune.service.result;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.UnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalUnsepuriResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UnsepuriService {

	private final RestTemplate restTemplate; // 인증서 무시 설정이 적용된 RestTemplate
	private final String apiUrl;

	public UnsepuriService(RestTemplate restTemplate, @Value("${external.api.unsepuri.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public UnsepuriResponse getUnsepuriResult(FortuneApiRequest request) {
		ExternalUnsepuriResponse apiResponse = callExternalApi(request);
		// 데이터를 UnsepuriResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출 메서드
	private ExternalUnsepuriResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalUnsepuriResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalUnsepuriResponse.class);

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

	// 외부 API 응답 데이터를 처리하여 내부 응답 DTO로 변환
	private UnsepuriResponse processApiResponse(ExternalUnsepuriResponse apiResponse) {
		ExternalUnsepuriResponse.Result result = apiResponse.getResult();

		return new UnsepuriResponse(
			buildAvoidPeople(result.getAvoidPeople()), // 피해야 할 상대
			buildCurrentUnsepuri(result.getCurrentUnsepuri()), // 현재 운세 풀이
			buildLuckElement(result.getLuckElement()) // 행운의 요소
		);
	}

	// 피해야 할 상대 데이터 처리
	private UnsepuriResponse.AvoidPeople buildAvoidPeople(String avoidPeople) {
		if (avoidPeople == null)
			return null;

		return new UnsepuriResponse.AvoidPeople("피해야 할 상대", avoidPeople);
	}

	// 현재 운세 풀이 데이터 처리
	private UnsepuriResponse.CurrentUnsepuri buildCurrentUnsepuri(ExternalUnsepuriResponse.CurrentUnsepuri external) {
		if (external == null)
			return null;

		return new UnsepuriResponse.CurrentUnsepuri(
			"현재 운세 풀이",
			new UnsepuriResponse.CurrentUnsepuri.Text("풀이 설명", external.getText()),
			new UnsepuriResponse.CurrentUnsepuri.Value("운세 점수", external.getValue())
		);
	}

	// 행운의 요소 데이터 처리
	private UnsepuriResponse.LuckElement buildLuckElement(ExternalUnsepuriResponse.LuckElement external) {
		if (external == null)
			return null;

		return new UnsepuriResponse.LuckElement(
			"행운의 요소",
			new UnsepuriResponse.LuckElement.Text("행운 요소 설명", external.getText()),
			new UnsepuriResponse.LuckElement.Value("행운 점수", external.getValue())
		);
	}
}