package com.palbang.unsemawang.chemistry.testinsert;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.palbang.unsemawang.fortune.dto.request.FortuneInfoUpdateRequest;
import com.palbang.unsemawang.fortune.dto.response.FortuneUserInfoUpdateResponse;
import com.palbang.unsemawang.fortune.entity.FortuneUserInfo;
import com.palbang.unsemawang.fortune.repository.FortuneUserInfoRepository;
import com.palbang.unsemawang.fortune.service.FortuneUserInfoUpdateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsertDayGanZhi {
	private final FortuneUserInfoRepository fortuneUserInfoRepository;
	private final FortuneUserInfoUpdateService fortuneUserInfoUpdateService;

	public int batchUpdateDayGanZhiForNullValues() {
		// 1. dayGan 또는 dayZhi가 null인 데이터 조회
		List<FortuneUserInfo> usersToUpdate = fortuneUserInfoRepository.findByDayGanIsNullOrDayZhiIsNull();

		log.info("총 {}개의 데이터를 업데이트합니다.", usersToUpdate.size());

		int updatedCount = 0;

		for (FortuneUserInfo user : usersToUpdate) {
			try {
				updateSingleUser(user); // 개별 업데이트 메서드 호출 (별도 트랜잭션 적용)
				updatedCount++;
			} catch (Exception e) {
				log.error("ID {} 업데이트 실패: {}", user.getId(), e.getMessage());
			}
		}

		log.info("업데이트 완료: 총 {}개의 데이터", updatedCount);
		return updatedCount;
	}

	// 개별 트랜잭션 적용 (하나만 실패해도 다른 업데이트에 영향 X)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateSingleUser(FortuneUserInfo user) {
		FortuneInfoUpdateRequest request = FortuneInfoUpdateRequest.builder()
			.memberId(user.getMember().getId())
			.relationId(user.getId())
			.relationName(user.getRelation().getRelationName())
			.nickname(user.getNickname())
			.year(user.getYear())
			.month(user.getMonth())
			.day(user.getDay())
			.hour(user.getHour())
			.sex(user.getSex())
			.youn(user.getYoun())
			.solunar(user.getSolunar())
			.build();

		FortuneUserInfoUpdateResponse response = fortuneUserInfoUpdateService.updateFortuneUserInfo(request);
		log.info("ID {}: dayGanZhi 업데이트 완료 -> {}", user.getId(), response);
	}
}