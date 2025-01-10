package com.palbang.unsemawang.fortune.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListResponse;
import com.palbang.unsemawang.fortune.dto.response.ContentReadResponse;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "운세 컨텐츠")
@RestController
@RequestMapping("/fortune-contents")
public class FortuneContentController {

	private final FortuneContentService fortuneContentService;

	@Autowired
	public FortuneContentController(
		FortuneContentService fortuneContentService
	) {
		this.fortuneContentService = fortuneContentService;
	}

	@Operation(description = "운세 컨텐츠 목록 전체 조회하는 API 입니다", summary = "운세 컨텐츠 목록 조회 API")
	@GetMapping("")
	public ResponseEntity<List<FortuneContent>> readList() {

		// 컨텐츠 목록 조회
		List<FortuneContent> list = fortuneContentService.getList();

		return ResponseEntity.
			status(HttpStatus.OK)
			.body(
					list
				);
	}

	@Operation(description = "운세 컨텐츠 정보 상세 조회하는 API 입니다", summary = "운세 컨텐츠 정보 상세 조회 API")
	@GetMapping("/{id}")
	public ResponseEntity<FortuneContent> read(@PathVariable(name = "id") Long id) {

		// 컨텐츠 상세 조회
		FortuneContent fortuneContent = fortuneContentService.getContentById(id);

		return ResponseEntity.ok(
				fortuneContent
		);

	}

}

