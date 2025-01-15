package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.CommonResponse;
import com.palbang.unsemawang.fortune.dto.result.ApiResponse.InsaengResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalInsaengResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InsaengService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public InsaengService(RestTemplate restTemplate, @Value("${external.api.insaeng.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public InsaengResponse getInsaengResult(FortuneApiRequest request) {
		ExternalInsaengResponse apiResponse = callExternalApi(request);
		// 데이터를 업데이트된 InsaengResponse로 변환
		return processApiResponse(apiResponse);
	}

	private ExternalInsaengResponse callExternalApi(FortuneApiRequest request) {
		try {
			log.info("Requesting external API with data: {}", request);
			ExternalInsaengResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalInsaengResponse.class);
			log.info("Received response from external API: {}", response);
			return response;
		} catch (Exception e) {
			log.error(
				"Error while calling external API. URL: {}, Request: {}, Error: {}",
				apiUrl, request, e.getMessage(), e);
			throw e;
		}
	}

	private InsaengResponse processApiResponse(ExternalInsaengResponse apiResponse) {
		ExternalInsaengResponse.Result result = apiResponse.getResult();

		return new InsaengResponse(
			buildSimpleResponse("총평", result.getTotal().getText()),
			buildSimpleResponse("궁합", result.getGunghap().getText()),
			buildSimpleResponse("결혼 관련 정보", result.getMarryGung().getText()),
			buildLuck(result.getLuck()),
			buildSimpleResponse("별자리", result.getConstellation()),
			buildSimpleResponse("현재의 길흉사", result.getCurrentGoodAndBadNews().getText()),
			buildSimpleResponse("팔복궁", result.getEightStar()),
			buildSimpleResponse("풍수로 보는 길흉", result.getGoodAndBadByPungsu()),
			buildSimpleResponse("천생연분", result.getSoulMates().getText())
		);
	}

	private CommonResponse buildLuck(ExternalInsaengResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new CommonResponse(
			"행운", "",
			List.of(
				new CommonResponse("성격", externalLuck.getPersonality(), null),
				new CommonResponse("직업운", externalLuck.getJob(), null),
				new CommonResponse("건강운", externalLuck.getHealth().getText(), null)
			)
		);
	}

	private CommonResponse buildSimpleResponse(String label, String value) {
		if (value == null)
			return null;
		return new CommonResponse(label, value, null);
	}
}