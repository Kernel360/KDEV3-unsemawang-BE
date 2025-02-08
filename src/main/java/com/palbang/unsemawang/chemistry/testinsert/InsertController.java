package com.palbang.unsemawang.chemistry.testinsert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/fortune")
@RequiredArgsConstructor
public class InsertController {
	private final InsertDayGanZhi updateService;

	@PutMapping("/update-daygan-zhi")
	public ResponseEntity<String> batchUpdateDayGanZhi() {
		int updatedCount = updateService.batchUpdateDayGanZhiForNullValues();
		return ResponseEntity.ok("총 " + updatedCount + "개의 데이터를 업데이트했습니다.");
	}
}
