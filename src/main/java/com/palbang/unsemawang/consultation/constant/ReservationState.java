package com.palbang.unsemawang.consultation.constant;

public enum ReservationState {
	PENDING(1, "예약대기"),
	CONFIRMED(2, "확정됨"),
	IN_PROGRESS(3, "상담진행중"),
	COMPLETED(4, "완료됨"),
	NO_SHOW(4, "노쇼"),
	CANCELED(5, "취소됨"),
	EXPIRED(5, "만료됨");

	private final int order;
	private final String s;

	ReservationState(int order, String s) {
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
