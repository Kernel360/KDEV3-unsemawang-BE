package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

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

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public UnsepuriService(RestTemplate restTemplate, @Value("${external.api.unsepuri.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	// Unsepuri 결과 반환
	public UnsepuriResponse getUnsepuriResult(FortuneApiRequest request) {
		ExternalUnsepuriResponse apiResponse = callExternalApi(request);
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출
	private ExternalUnsepuriResponse callExternalApi(FortuneApiRequest request) {
		try {
			log.info("Requesting external API with data: {}", request);
			ExternalUnsepuriResponse response = restTemplate.postForObject(
				apiUrl, request, ExternalUnsepuriResponse.class);
			log.info("Received response from external API: {}", response);
			return response;
		} catch (Exception e) {
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	// API 응답 데이터를 UnsepuriResponse로 변환
	private UnsepuriResponse processApiResponse(ExternalUnsepuriResponse apiResponse) {
		ExternalUnsepuriResponse.Result result = apiResponse.getResult();

		return new UnsepuriResponse(
			buildAvoidPeople(result.getAvoidPeople()), // 피해야 할 상대
			buildCurrentUnsepuri(result.getCurrentUnsepuri()), // 현재 운세 풀이
			buildLuckElement(result.getLuckElement()) // 행운의 요소
		);
	}

	// 피해야 할 상대 처리
	private UnsepuriResponse.AvoidPeople buildAvoidPeople(String avoidPeople) {
		if (avoidPeople == null)
			return null;

		return new UnsepuriResponse.AvoidPeople("피해야 할 상대", avoidPeople);
	}

	// 현재 운세 풀이 처리
	private UnsepuriResponse.CurrentUnsepuri buildCurrentUnsepuri(ExternalUnsepuriResponse.CurrentUnsepuri external) {
		if (external == null)
			return null;

		// children 리스트 생성
		List<UnsepuriResponse.CurrentUnsepuri.Children> children = List.of(
			new UnsepuriResponse.CurrentUnsepuri.Children(
				new UnsepuriResponse.CurrentUnsepuri.Children.Text("풀이 설명", external.getText()),
				new UnsepuriResponse.CurrentUnsepuri.Children.Value("운세 점수", external.getValue())
			)
		);

		return new UnsepuriResponse.CurrentUnsepuri("현재 운세 풀이", children);
	}

	// 행운의 요소 처리
	private UnsepuriResponse.LuckElement buildLuckElement(ExternalUnsepuriResponse.LuckElement external) {
		if (external == null)
			return null;

		// children 리스트 생성
		List<UnsepuriResponse.LuckElement.Children> children = List.of(
			new UnsepuriResponse.LuckElement.Children(
				new UnsepuriResponse.LuckElement.Children.Text("행운 요소 설명", external.getText()),
				new UnsepuriResponse.LuckElement.Children.Value("행운 점수", external.getValue())
			)
		);

		return new UnsepuriResponse.LuckElement("행운의 요소", children);
	}
}