package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SajuunseService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public SajuunseService(RestTemplate restTemplate, @Value("${external.api.sajuunse.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public SajuunseResponse getSajuunseResult(FortuneApiRequest request) {
		ExternalSajuunseResponse apiResponse = callExternalApi(request);
		// 데이터를 SajuunseResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출 처리
	private ExternalSajuunseResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalSajuunseResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalSajuunseResponse.class);

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

	// 응답 가공 로직
	private SajuunseResponse processApiResponse(ExternalSajuunseResponse apiResponse) {
		ExternalSajuunseResponse.Result result = apiResponse.getResult();

		return new SajuunseResponse(
			buildSimpleResponse("태어난 계절에 따른 운", result.getBornSeasonLuck()), // 태어난 계절에 따른 운
			buildLuck(result.getLuck()), // 행운
			buildSimpleResponse("타고난 성품", result.getNatureCharacter()), // 타고난 성품
			buildSimpleResponse("사회적 특성", result.getSocialCharacter()), // 사회적 특성
			buildSimpleResponse("사회적 성격", result.getSocialPersonality()), // 사회적 성격
			buildSimpleResponse("피해야 할 상대", result.getAvoidPeople()), // 피해야 할 상대
			buildSimpleResponse("현재 나의 운 분석", result.getCurrentLuckAnalysis().getText()) // 현재 나의 운 분석
		);
	}

	// 행운 처리
	private CommonResponse buildLuck(ExternalSajuunseResponse.Result.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new CommonResponse(
			"행운", "",
			List.of(
				new CommonResponse("혈연", externalLuck.getBloodRelative(), null),
				new CommonResponse("직업", externalLuck.getJob(), null),
				new CommonResponse("성격", externalLuck.getPersonality(), null),
				new CommonResponse("애정운", externalLuck.getAffection(), null),
				new CommonResponse("건강운", externalLuck.getHealth(), null),
				new CommonResponse("길운 성씨", externalLuck.getGoodLuckFamilyName(), null)
			)
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}