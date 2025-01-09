package com.palbang.unsemawang.fortune.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.fortune.dto.request.SearchRequest;
import com.palbang.unsemawang.fortune.dto.response.ContentReadListResponse;
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

	public List<FortuneContent> getList() {
		return fortuneContentRepository.findAll();
	}

	public FortuneContent getContentById(Long id) {
		return fortuneContentRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "id :" + id));
	}

	public ContentReadListResponse getSearchList(SearchRequest searchRequest) {

		// 검색 키워드를 포함한 이름을 가진 컨텐츠 목록 조회
		List<FortuneContent> findList = fortuneContentRepository.findByKeyword(searchRequest.keyword());

		return ContentReadListResponse.of(findList);
	}

}
