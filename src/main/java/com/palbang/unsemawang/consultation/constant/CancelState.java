package com.palbang.unsemawang.consultation.constant;

public enum CancelState {
	REQUESTED(0, "요청됨"),
	PENDING(1, "처리중"),
	COMPLETED(2, "처리완료"),
	FAILED(2, "처리실패");

	private final int order;
	private final String s;

	CancelState(int order, String s) {
		this.order = order;
		this.s = s;
	}

	public int getOrder() {
		return this.order;
	}

	public String toString() {
		return name();
	}
}
