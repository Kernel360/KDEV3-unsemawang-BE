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

	private final RestTemplate restTemplate; // 인증서 무시 설정이 적용된 RestTemplate
	private final String apiUrl;

	public TojeongService(RestTemplate restTemplate, @Value("${external.api.tojeong.url}") String apiUrl) {
		this.restTemplate = restTemplate;
		this.apiUrl = apiUrl;
	}

	public TojeongResponse getTojeongResult(FortuneApiRequest request) {
		ExternalTojeongResponse apiResponse = callExternalApi(request);
		// 데이터를 TojeongResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	private ExternalTojeongResponse callExternalApi(FortuneApiRequest request) {
		try {
			// 요청 데이터 로그
			log.info("Requesting external API with data: {}", request);

			// API 호출
			ExternalTojeongResponse response = restTemplate.postForObject(apiUrl, request,
				ExternalTojeongResponse.class);

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

	private TojeongResponse processApiResponse(ExternalTojeongResponse apiResponse) {
		ExternalTojeongResponse.Result result = apiResponse.getResult();

		return new TojeongResponse(
			buildCurrentLuckAnalysis(result.getCurrentLuckAnalysis()), // 현재 나의 운 분석
			buildThisYearLuck(result.getThisYearLuck()), // 올해의 운세
			buildTojeongSecret(result.getTojeongSecret()), // 토정비결
			buildWealth(result.getWealth()), // 재물
			new TojeongResponse.NatureCharacter("타고난 성품", result.getNatureCharacter()),
			new TojeongResponse.CurrentBehavior("현재 지켜야 할 처세", result.getCurrentBehavior()),
			new TojeongResponse.CurrentHumanRelationship("현재 대인 관계", result.getCurrentHumanRelationship()),
			new TojeongResponse.AvoidPeople("피해야 할 상대", result.getAvoidPeople())
		);
	}

	private TojeongResponse.CurrentLuckAnalysis buildCurrentLuckAnalysis(
		ExternalTojeongResponse.CurrentLuckAnalysis external) {
		if (external == null)
			return null;

		return new TojeongResponse.CurrentLuckAnalysis(
			"현재 나의 운 분석",
			new TojeongResponse.CurrentLuckAnalysis.Text("운세 설명", external.getText()),
			new TojeongResponse.CurrentLuckAnalysis.Value("운세 값", external.getValue())
		);
	}

	private TojeongResponse.ThisYearLuck buildThisYearLuck(ExternalTojeongResponse.TojeongThisYearLuck external) {
		if (external == null)
			return null;

		// 1월~12월 데이터 매핑 (Month 객체 리스트 생성)
		List<TojeongResponse.ThisYearLuck.Children.MonthlyLuck.Month> monthList = external.getMonth()
			.stream()
			.map(month -> new TojeongResponse.ThisYearLuck.Children.MonthlyLuck.Month(
				month.getMonth(),  // 1월, 2월 형식으로 label 생성
				month.getValue()
			))
			.toList();

		TojeongResponse.ThisYearLuck.Children.MonthlyLuck monthlyLuck = new TojeongResponse.ThisYearLuck.Children.MonthlyLuck(
			"월별 운세",
			monthList
		);

		// ThisYearLuck 반환
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
}