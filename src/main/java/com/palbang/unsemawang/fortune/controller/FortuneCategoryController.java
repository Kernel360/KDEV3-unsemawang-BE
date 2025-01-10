package com.palbang.unsemawang.fortune.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.response.Response;
import com.palbang.unsemawang.fortune.dto.response.CategoryReadListResponse;
import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.service.FortuneCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "운세 분야")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fortune-category")
public class FortuneCategoryController {

	private final FortuneCategoryService fortuneCategoryService;

	@Operation(
		summary = "운세 분야 목록 조회",
		description = "공개 처리된 운세 분야 목록을 조회한다",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			)
		}
	)
	@GetMapping
	public ResponseEntity<List<FortuneCategory>> readList() {

		// 공개 처리 된 카테고리 목록 조회
		List<FortuneCategory> fortuneCategoryList = fortuneCategoryService.getVisibleList();

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(
				fortuneCategoryList
				);
	}

}
