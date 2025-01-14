package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
			buildTotal(result.getTotal()),
			buildGunghap(result.getGunghap()),
			buildMarryGung(result.getMarryGung()),
			buildLuck(result.getLuck()),
			buildConstellation(result.getConstellation()),
			buildCurrentGoodAndBadNews(result.getCurrentGoodAndBadNews()),
			buildEightStar(result.getEightStar()),
			buildGoodAndBadByPungsu(result.getGoodAndBadByPungsu()),
			buildSoulMates(result.getSoulMates())
		);
	}

	private InsaengResponse.Total buildTotal(ExternalInsaengResponse.Total externalTotal) {
		if (externalTotal == null)
			return null;

		return new InsaengResponse.Total(
			"총평",
			List.of(
				new InsaengResponse.Total.Children(
					new InsaengResponse.Total.Children.Text("운세 설명", externalTotal.getText()),
					new InsaengResponse.Total.Children.Value("운세 값", externalTotal.getValue())
				)
			)
		);
	}

	private InsaengResponse.Gunghap buildGunghap(ExternalInsaengResponse.Gunghap externalGunghap) {
		if (externalGunghap == null)
			return null;

		return new InsaengResponse.Gunghap(
			"궁합",
			List.of(
				new InsaengResponse.Gunghap.Children(
					new InsaengResponse.Gunghap.Children.Text("운세 설명", externalGunghap.getText()),
					new InsaengResponse.Gunghap.Children.Value("운세 값", externalGunghap.getValue())
				)
			)
		);
	}

	private InsaengResponse.MarryGung buildMarryGung(ExternalInsaengResponse.MarryGung externalMarryGung) {
		if (externalMarryGung == null)
			return null;

		return new InsaengResponse.MarryGung(
			"결혼 관련 정보",
			List.of(
				new InsaengResponse.MarryGung.Children(
					new InsaengResponse.MarryGung.Children.Text("운세 설명", externalMarryGung.getText()),
					new InsaengResponse.MarryGung.Children.Value("운세 값", externalMarryGung.getValue())
				)
			)
		);
	}

	private InsaengResponse.Luck buildLuck(ExternalInsaengResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new InsaengResponse.Luck(
			"행운",
			List.of(
				new InsaengResponse.Luck.Children(
					new InsaengResponse.Luck.Children.Personality("성격", externalLuck.getPersonality()),
					new InsaengResponse.Luck.Children.Job("직업운", externalLuck.getJob()),
					new InsaengResponse.Luck.Children.Health("건강운", externalLuck.getHealth().getText())
				)
			)
		);
	}

	private InsaengResponse.Constellation buildConstellation(String constellation) {
		if (constellation == null)
			return null;

		return new InsaengResponse.Constellation("별자리", constellation);
	}

	private InsaengResponse.CurrentGoodAndBadNews buildCurrentGoodAndBadNews(
		ExternalInsaengResponse.CurrentGoodAndBadNews externalGoodAndBadNews) {
		if (externalGoodAndBadNews == null)
			return null;

		return new InsaengResponse.CurrentGoodAndBadNews(
			"현재의 길흉사",
			externalGoodAndBadNews.getText()
		);
	}

	private InsaengResponse.EightStar buildEightStar(String eightStar) {
		if (eightStar == null)
			return null;

		return new InsaengResponse.EightStar("팔복궁", eightStar);
	}

	private InsaengResponse.GoodAndBadByPungsu buildGoodAndBadByPungsu(String goodAndBadByPungsu) {
		if (goodAndBadByPungsu == null)
			return null;

		return new InsaengResponse.GoodAndBadByPungsu("풍수로 보는 길흉", goodAndBadByPungsu);
	}

	private InsaengResponse.SoulMates buildSoulMates(ExternalInsaengResponse.SoulMates externalSoulMates) {
		if (externalSoulMates == null)
			return null;

		return new InsaengResponse.SoulMates(
			"천생연분",
			externalSoulMates.getText()
		);
	}
}