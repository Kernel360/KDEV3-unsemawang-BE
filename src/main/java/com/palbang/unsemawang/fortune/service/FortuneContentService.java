package com.palbang.unsemawang.fortune.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
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

}
