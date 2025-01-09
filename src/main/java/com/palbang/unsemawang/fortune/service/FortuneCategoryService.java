package com.palbang.unsemawang.fortune.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.fortune.entity.FortuneCategory;
import com.palbang.unsemawang.fortune.repository.FortuneCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FortuneCategoryService {
	private final FortuneCategoryRepository fortuneCategoryRepository;

	public List<FortuneCategory> getVisibleList() {
		return fortuneCategoryRepository.findAllByIsVisibleIsTrue();
	}
}
