package com.palbang.unsemawang.fortune.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.fortune.dto.request.SearchRequest;
import com.palbang.unsemawang.fortune.dto.response.ContentReadDetailDto;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListDto;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 컨텐츠")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fortune-contents")
public class FortuneContentController {

	private final FortuneContentService fortuneContentService;

	@Operation(
		description = "현재는 이름에 검색 키워드가 포함된 컨텐츠 리스트가 조회 됩니다. 정렬 조건은 추후 추가될 예정입니다",
		summary = "운세 컨텐츠 검색 API"
	)
	@GetMapping("/search")
	public ResponseEntity<List<ContentReadListDto>> searchFortuneContent(
		@ParameterObject @ModelAttribute SearchRequest searchRequest
	) {
		// 키워드를 포함하는 이름을 가진 컨텐츠 목록 가져오기
		List<ContentReadListDto> searchResult = fortuneContentService.getSearchList(searchRequest);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(searchResult);
	}

	@Operation(
		description = "운세 컨텐츠 목록 조회하는 API 입니다. 카테고리명을 입력할 경우 카테고리에 따른 컨텐츠 목록이, 입력하지 않을 경우 전체 목록이 조회됩니다",
		summary = "운세 컨텐츠 목록 조회 API")
	@GetMapping
	public ResponseEntity<List<ContentReadListDto>> readFortuneContentList(
		@RequestParam(name = "classification", required = false) String categoryName
	) {

		// 컨텐츠 목록 조회
		List<ContentReadListDto> list = fortuneContentService.getList(categoryName);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(list);
	}

	@Operation(
		description = "운세 컨텐츠 정보 상세 조회하는 API 입니다",
		summary = "운세 컨텐츠 정보 상세 조회 API",
		parameters = {@Parameter(name = "id", description = "운세 컨텐츠 ID", required = true)}
	)
	@GetMapping("/{id}")
	public ResponseEntity<ContentReadDetailDto> readFortuneContentInfo(@PathVariable(name = "id") Long id) {

		// 컨텐츠 상세 조회
		ContentReadDetailDto fortuneContent = fortuneContentService.getContentById(id);

		return ResponseEntity.ok(fortuneContent);

	}

}

