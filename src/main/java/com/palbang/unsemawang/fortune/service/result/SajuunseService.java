package com.palbang.unsemawang.fortune.service.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.palbang.unsemawang.fortune.dto.result.ApiResponse.SajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse.ExternalSajuunseResponse;
import com.palbang.unsemawang.fortune.dto.result.FortuneApiRequest;

@Service
public class SajuunseService {

	private final RestTemplate restTemplate = new RestTemplate(); // 외부 API 호출용
	private final String apiUrl;

	public SajuunseService(@Value("${external.api.sajuunse.url}") String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public SajuunseResponse getSajuunseResult(FortuneApiRequest request) {
		ExternalSajuunseResponse apiResponse = callExternalApi(request);
		// 데이터를 SajuunseResponse로 변환하여 반환
		return processApiResponse(apiResponse);
	}

	// 외부 API 호출 처리
	private ExternalSajuunseResponse callExternalApi(FortuneApiRequest request) {
		return restTemplate.postForObject(apiUrl, request, ExternalSajuunseResponse.class);
	}

	// 응답 가공 로직
	private SajuunseResponse processApiResponse(ExternalSajuunseResponse apiResponse) {
		ExternalSajuunseResponse.Result result = apiResponse.getResult();

		return new SajuunseResponse(
			buildBornSeasonLuck(result.getBornSeasonLuck()), // 태어난 계절에 따른 운
			buildLuck(result.getLuck()), // 행운
			buildNaturalCharacter("타고난 성품", result.getNatureCharacter()), // 타고난 성품
			buildSocialCharacter("사회적 특성", result.getSocialCharacter()), // 사회적 특성
			buildSocialPersonality("사회적 성격", result.getSocialPersonality()), // 사회적 성격
			buildAvoidPeople("피해야 할 상대", result.getAvoidPeople()), // 피해야 할 상대
			buildCurrentLuckAnalysis(result.getCurrentLuckAnalysis()) // 현재 나의 운 분석
		);
	}

	// 태어난 계절에 따른 운 처리
	private SajuunseResponse.BornSeasonLuck buildBornSeasonLuck(String bornSeasonLuck) {
		if (bornSeasonLuck == null)
			return null;

		return new SajuunseResponse.BornSeasonLuck("태어난 계절에 따른 운", bornSeasonLuck);
	}

	// 행운 처리
	private SajuunseResponse.Luck buildLuck(ExternalSajuunseResponse.Result.Luck externalLuck) {
		if (externalLuck == null)
			return null;

		return new SajuunseResponse.Luck(
			"행운",
			List.of(new SajuunseResponse.Luck.Children(
				new SajuunseResponse.Luck.Children.BloodRelative("혈연", externalLuck.getBloodRelative()),
				new SajuunseResponse.Luck.Children.Job("직업", externalLuck.getJob()),
				new SajuunseResponse.Luck.Children.Personality("성격", externalLuck.getPersonality()),
				new SajuunseResponse.Luck.Children.Affection("애정운", externalLuck.getAffection()),
				new SajuunseResponse.Luck.Children.Health("건강운", externalLuck.getHealth()),
				new SajuunseResponse.Luck.Children.GoodLuckFamilyName("길운 성씨", externalLuck.getGoodLuckFamilyName())
			))
		);
	}

	// 단순 텍스트 처리
	private SajuunseResponse.NatureCharacter buildNaturalCharacter(String label, String value) {
		if (value == null)
			return null;

		return new SajuunseResponse.NatureCharacter(label, value);
	}

	// 사회적 특성 처리
	private SajuunseResponse.SocialCharacter buildSocialCharacter(String label, String value) {
		if (value == null)
			return null;

		return new SajuunseResponse.SocialCharacter(label, value);
	}

	// 사회적 성격 처리
	private SajuunseResponse.SocialPersonality buildSocialPersonality(String label, String value) {
		if (value == null)
			return null;

		return new SajuunseResponse.SocialPersonality(label, value);
	}

	// 피해야 할 상대 처리
	private SajuunseResponse.AvoidPeople buildAvoidPeople(String label, String value) {
		if (value == null)
			return null;

		return new SajuunseResponse.AvoidPeople(label, value);
	}

	// 현재 나의 운 분석 처리
	private SajuunseResponse.CurrentLuckAnalysis buildCurrentLuckAnalysis(
		ExternalSajuunseResponse.Result.CurrentLuckAnalysis externalAnalysis
	) {
		if (externalAnalysis == null)
			return null;

		return new SajuunseResponse.CurrentLuckAnalysis(
			"현재 나의 운 분석",
			List.of(new SajuunseResponse.CurrentLuckAnalysis.Children(
				new SajuunseResponse.CurrentLuckAnalysis.Children.Text("운세 설명", externalAnalysis.getText()),
				new SajuunseResponse.CurrentLuckAnalysis.Children.Value("운세 값", externalAnalysis.getValue())
			))
		);
	}
}