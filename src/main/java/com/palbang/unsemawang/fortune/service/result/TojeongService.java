package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.TojeongResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalTojeongResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TojeongService {

	private final RestTemplate restTemplate;
	private final String apiUrl;

	public TojeongService(RestTemplate restTemplate, @Value("${external.api.tojeong.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public TojeongResponse getTojeongResult(FortuneApiRequest request) {
		ExternalTojeongResponse apiResponse = callExternalApi(request);
		return processApiResponse(apiResponse);
	}

	private ExternalTojeongResponse callExternalApi(FortuneApiRequest request) {
		try {
			log.info("Requesting external API with data: {}", request);

			ExternalTojeongResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalTojeongResponse.class);

			log.info("Received response from external API: {}", response);

			return response;
		} catch (Exception e) {
			log.error("Error while calling external API. URL: {}, Request: {}, Error: {}", apiUrl, request,
				e.getMessage(), e);
			throw e;
		}
	}

	private TojeongResponse processApiResponse(ExternalTojeongResponse apiResponse) {
		ExternalTojeongResponse.Result result = apiResponse.getResult();

		return new TojeongResponse(
			buildCurrentLuckAnalysis(result.getCurrentLuckAnalysis()),
			buildThisYearLuck(result.getThisYearLuck()),
			buildTojeongSecret(result.getTojeongSecret()),
			buildWealth(result.getWealth()),
			buildNatureCharacter("타고난 성품", result.getNatureCharacter()),
			buildSimpleField("현재 지켜야 할 처세", result.getCurrentBehavior(), TojeongResponse.CurrentBehavior.class),
			buildSimpleField("현재 대인 관계", result.getCurrentHumanRelationship(),
				TojeongResponse.CurrentHumanRelationship.class),
			buildSimpleField("피해야 할 상대", result.getAvoidPeople(), TojeongResponse.AvoidPeople.class)
		);
	}

	private TojeongResponse.CurrentLuckAnalysis buildCurrentLuckAnalysis(
		ExternalTojeongResponse.CurrentLuckAnalysis external) {
		if (external == null)
			return null;

		// children 생성 (Text 및 Value를 포함하는 리스트)
		List<TojeongResponse.CurrentLuckAnalysis.Children> children = List.of(
			new TojeongResponse.CurrentLuckAnalysis.Children(
				new TojeongResponse.CurrentLuckAnalysis.Children.Text("운세 설명", external.getText()),
				new TojeongResponse.CurrentLuckAnalysis.Children.Value("운세 값", external.getValue())
			)
		);

		// CurrentLuckAnalysis 반환
		return new TojeongResponse.CurrentLuckAnalysis(
			"현재 나의 운 분석",
			children
		);
	}

	private TojeongResponse.ThisYearLuck buildThisYearLuck(ExternalTojeongResponse.TojeongThisYearLuck external) {
		if (external == null)
			return null;

		List<TojeongResponse.ThisYearLuck.Children.MonthlyLuck.Month> monthList = external.getMonth().stream()
			.map(month -> new TojeongResponse.ThisYearLuck.Children.MonthlyLuck.Month(month.getMonth(),
				month.getValue()))
			.toList();

		TojeongResponse.ThisYearLuck.Children.MonthlyLuck monthlyLuck = new TojeongResponse.ThisYearLuck.Children.MonthlyLuck(
			"월별 운세",
			monthList
		);

		return new TojeongResponse.ThisYearLuck(
			"올해의 운세",
			List.of(new TojeongResponse.ThisYearLuck.Children(
				new TojeongResponse.ThisYearLuck.Children.RomanticRelationship("연애운",
					external.getRomanticRelationship()),
				new TojeongResponse.ThisYearLuck.Children.Health("건강운", external.getHealth()),
				new TojeongResponse.ThisYearLuck.Children.Company("직장운", external.getCompany()),
				new TojeongResponse.ThisYearLuck.Children.Hope("소망운", external.getHope()),
				monthlyLuck
			))
		);
	}

	private TojeongResponse.TojeongSecret buildTojeongSecret(ExternalTojeongResponse.TojeongSecret external) {
		if (external == null)
			return null;

		return new TojeongResponse.TojeongSecret(
			"토정비결",
			List.of(new TojeongResponse.TojeongSecret.Children(
				new TojeongResponse.TojeongSecret.Children.FirstHalf("일년신수(전반기)", external.getFirstHalf()),
				new TojeongResponse.TojeongSecret.Children.SecondHalf("일년신수(후반기)", external.getSecondHalf())
			))
		);
	}

	private TojeongResponse.Wealth buildWealth(ExternalTojeongResponse.Wealth external) {
		if (external == null)
			return null;

		return new TojeongResponse.Wealth(
			"재물 운세",
			List.of(new TojeongResponse.Wealth.Children(
				new TojeongResponse.Wealth.Children.Characteristics("재물 운의 특성", external.getCharacteristics()),
				new TojeongResponse.Wealth.Children.Accumulate("재물 모으는 법", external.getAccumulate()),
				new TojeongResponse.Wealth.Children.Prevent("재물 손실 방지", external.getPrevent()),
				new TojeongResponse.Wealth.Children.Invesetment("재테크", external.getInvestment()),
				new TojeongResponse.Wealth.Children.CurrentLuck("현재 재물운", external.getCurrentLuck())
			))
		);
	}

	private <T> T buildSimpleField(String label, String value, Class<T> clazz) {
		if (value == null)
			return null;

		try {
			// Reflection을 사용해 기본 String 기반 생성자 호출
			return clazz.getConstructor(String.class, String.class).newInstance(label, value);
		} catch (Exception e) {
			log.error("Error while building simple field for {}: {}", clazz.getSimpleName(), e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private TojeongResponse.NatureCharacter buildNatureCharacter(String label, String value) {
		if (value == null)
			return null;

		return new TojeongResponse.NatureCharacter(label, value);
	}
}