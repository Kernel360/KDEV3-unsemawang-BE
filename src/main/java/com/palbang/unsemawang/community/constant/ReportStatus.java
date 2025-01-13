package com.palbang.unsemawang.community.constant;

import lombok.Getter;

@Getter
public enum ReportStatus {
	RECEIVED("신고 접수"),        // 신고가 접수된 상태
	IN_PROGRESS("신고 처리 중"), // 신고 처리가 진행 중인 상태
	COMPLETED("처리 완료"),     // 신고 처리가 완료된 상태
	REJECTED("신고 반려");      // 신고가 반려된 상태

	private final String description;

	ReportStatus(String description) {
		this.description = description;
	}
}
