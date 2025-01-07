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
import com.palbang.unsemawang.common.response.DataResponse;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListResponse;
import com.palbang.unsemawang.fortune.dto.response.ContentReadResponse;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.service.FortuneContentService;

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

	@GetMapping
	public ResponseEntity<DataResponse<ContentReadListResponse>> readList() {

		// 컨텐츠 목록 조회
		List<FortuneContent> list = fortuneContentService.getList();

		return ResponseEntity.
			status(HttpStatus.OK)
			.body(
				DataResponse.of(
					ResponseCode.SUCCESS_SEARCH,
					ContentReadListResponse.of(list)
				)
			);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DataResponse<ContentReadResponse>> read(@PathVariable Long id) {

		// 컨텐츠 상세 조회
		FortuneContent fortuneContent = fortuneContentService.getContentById(id);

		return ResponseEntity.ok(
			DataResponse.of(
				ResponseCode.SUCCESS_SEARCH,
				ContentReadResponse.of(fortuneContent)
			)
		);

	}

}

