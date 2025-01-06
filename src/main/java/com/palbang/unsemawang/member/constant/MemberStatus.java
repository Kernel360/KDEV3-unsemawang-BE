package com.palbang.unsemawang.member.constant;

public enum MemberStatus {
	ACTIVE("활성화된 상태로 정상적으로 이용 가능"),
	INACTIVE("비활성화된 상태로 사용자가 의도적으로 비활성화한 경우"),
	LOCKED("계정이 잠겨 로그인 및 서비스 이용이 제한된 상태"),
	SUSPENDED("일시적으로 정지된 상태 (관리자 또는 정책 위반)"),
	DELETED("삭제된 계정 (복구 불가)");

	private final String description;

	MemberStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
