package com.palbang.unsemawang.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.community.dto.response.PostDetailResponse;
import com.palbang.unsemawang.community.service.PostDetailService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/posts")
public class PostDetailController {

	private final PostDetailService postDetailService;

	public PostDetailController(PostDetailService postDetailService) {
		this.postDetailService = postDetailService;
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long id) {
		try {
			PostDetailResponse response = postDetailService.getPostDetail(id);
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException ex) {
			throw new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "게시글을 찾을 수 없습니다.");
		}
	}
}
