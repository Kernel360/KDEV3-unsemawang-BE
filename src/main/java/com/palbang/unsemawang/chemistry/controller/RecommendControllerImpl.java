package com.palbang.unsemawang.chemistry.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chemistry.dto.response.ChemistryRecommendResponse;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chemistry")
@RequiredArgsConstructor
public class RecommendControllerImpl implements RecommendController {

	@GetMapping("/recommendations")
	@Override
	public ResponseEntity<List<ChemistryRecommendResponse>> recommendChemistryMember(
		@AuthenticationPrincipal CustomOAuth2User user
	) {

		List<ChemistryRecommendResponse> recommendedMemberList = new ArrayList<>();

		return ResponseEntity.ok(recommendedMemberList);
	}
}
