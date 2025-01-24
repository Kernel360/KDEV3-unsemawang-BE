package com.palbang.unsemawang.fortune.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.SearchRequest;
import com.palbang.unsemawang.fortune.dto.response.ContentReadDetailDto;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListDto;
import com.palbang.unsemawang.fortune.entity.FortuneContent;
import com.palbang.unsemawang.fortune.repository.FortuneContentRepository;

@Service
public class FortuneContentService {
	private final FortuneContentRepository fortuneContentRepository;

	@Autowired
	public FortuneContentService(
		FortuneContentRepository fortuneContentRepository
	) {
		this.fortuneContentRepository = fortuneContentRepository;
	}

	public List<ContentReadListDto> getList() {
		List<FortuneContent> findList = fortuneContentRepository.findAll();

		return ContentReadListDto.of(findList);
	}

	public List<ContentReadListDto> getList(String categoryName) {

		// 카테고리명이 null 값인 경우 전체 조회
		if (categoryName == null) {
			return getList();
		}

		// 카테고리명이 있을 경우 필터링 조회
		List<FortuneContent> findListByCategory = fortuneContentRepository.findAllByFortuneCategory(categoryName);
		return ContentReadListDto.of(findListByCategory);
	}

	public ContentReadDetailDto getContentById(Long id) {

		FortuneContent findContent = fortuneContentRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "id :" + id));

		return ContentReadDetailDto.of(findContent);
	}

	public List<ContentReadListDto> getSearchList(SearchRequest searchRequest) {
		List<FortuneContent> findList;

		// 키워드와 카테고리명이 모두 존재할 경우
		if (searchRequest.getCategoryName() != null && !searchRequest.getCategoryName().isBlank()) {
			findList = fortuneContentRepository.findByKeywordAndFortuneCategory(
				searchRequest.getKeyword(),
				searchRequest.getCategoryName()
			);
		} else {
			// 카테고리 필터 없이 키워드를 기준으로 조회
			findList = fortuneContentRepository.findByKeyword(searchRequest.getKeyword());
		}

		return ContentReadListDto.of(findList);
	}
}
