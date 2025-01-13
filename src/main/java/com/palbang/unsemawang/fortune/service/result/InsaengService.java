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

	private final RestTemplate restTemplate = new RestTemplate(); // API 호출용
	private final String apiUrl;

	public InsaengService(@Value("${external.api.insaeng.url}") String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public InsaengResponse getInsaengResult(FortuneApiRequest request) {
		ExternalInsaengResponse apiResponse = callExternalApi(request);
		// 데이터를 InsaengResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출
	private ExternalInsaengResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalInsaengResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalInsaengResponse.class);

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

	// API 응답 데이터를 처리하여 응답 DTO로 변환
	private InsaengResponse processApiResponse(ExternalInsaengResponse apiResponse) {
		ExternalInsaengResponse.Result result = apiResponse.getResult();

		return new InsaengResponse(
			buildTotal(result.getTotal()), // 총평
			buildGunghap(result.getGunghap()), // 궁합
			buildMarryGung(result.getMarryGung()), // 결혼 관련 정보
			buildLuck(result.getLuck()), // 행운
			buildConstellation(result.getConstellation()), // 별자리
			buildCurrentGoodAndBadNews(result.getCurrentGoodAndBadNews()), // 현재의 길흉사
			buildEightStar(result.getEightStar()), // 팔복궁
			buildGoodAndBadByPungsu(result.getGoodAndBadByPungsu()), // 풍수로 보는 길흉
			buildSoulMates(result.getSoulMates()) // 천생연분
		);
	}

	// 총평 처리
	private InsaengResponse.Total buildTotal(ExternalInsaengResponse.Total externalTotal) {
		if (externalTotal == null)
			return null;

		return new InsaengResponse.Total(
			"총평",
			new InsaengResponse.Total.Text("운세 설명", externalTotal.getText()),
			new InsaengResponse.Total.Value("운세 값", externalTotal.getValue())
		);
	}

	// 궁합 처리
	private InsaengResponse.Gunghap buildGunghap(ExternalInsaengResponse.Gunghap externalGunghap) {
		if (externalGunghap == null)
			return null;

		return new InsaengResponse.Gunghap(
			"궁합",
			new InsaengResponse.Gunghap.Text("운세 설명", externalGunghap.getText()),
			new InsaengResponse.Gunghap.Value("운세 값", externalGunghap.getValue())
		);
	}

	// 결혼 관련 정보 처리
	private InsaengResponse.MarryGung buildMarryGung(ExternalInsaengResponse.MarryGung externalMarryGung) {
		if (externalMarryGung == null)
			return null;

		return new InsaengResponse.MarryGung(
			"결혼 관련 정보",
			new InsaengResponse.MarryGung.Text("운세 설명", externalMarryGung.getText()),
			new InsaengResponse.MarryGung.Value("운세 값", externalMarryGung.getValue())
		);
	}

	// 행운 처리
	private InsaengResponse.Luck buildLuck(ExternalInsaengResponse.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		// 자식 요소 처리 (성격, 직업운, 건강)
		return new InsaengResponse.Luck(
			"행운",
			List.of(new InsaengResponse.Luck.Children(
				new InsaengResponse.Luck.Children.Personality("성격", externalLuck.getPersonality()),
				new InsaengResponse.Luck.Children.Job("직업운", externalLuck.getJob()),
				new InsaengResponse.Luck.Children.Health("건강운", externalLuck.getHealth().getText())
			))
		);
	}

	// 별자리 처리
	private InsaengResponse.Constellation buildConstellation(String constellation) {
		if (constellation == null)
			return null;

		return new InsaengResponse.Constellation("별자리", constellation);
	}

	// 현재의 길흉사 처리
	private InsaengResponse.CurrentGoodAndBadNews buildCurrentGoodAndBadNews(
		ExternalInsaengResponse.CurrentGoodAndBadNews externalGoodAndBadNews) {
		if (externalGoodAndBadNews == null)
			return null;

		return new InsaengResponse.CurrentGoodAndBadNews(
			"현재의 길흉사",
			externalGoodAndBadNews.getText()
		);
	}

	// 팔복궁 처리
	private InsaengResponse.EightStar buildEightStar(String eightStar) {
		if (eightStar == null)
			return null;

		return new InsaengResponse.EightStar("팔복궁", eightStar);
	}

	// 풍수로 보는 길흉 처리
	private InsaengResponse.GoodAndBadByPungsu buildGoodAndBadByPungsu(String goodAndBadByPungsu) {
		if (goodAndBadByPungsu == null)
			return null;

		return new InsaengResponse.GoodAndBadByPungsu("풍수로 보는 길흉", goodAndBadByPungsu);
	}

	// 천생연분 처리
	private InsaengResponse.SoulMates buildSoulMates(ExternalInsaengResponse.SoulMates externalSoulMates) {
		if (externalSoulMates == null)
			return null;

		return new InsaengResponse.SoulMates(
			"천생연분",
			externalSoulMates.getText()
		);
	}
}