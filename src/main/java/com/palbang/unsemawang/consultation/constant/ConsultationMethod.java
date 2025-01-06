package com.palbang.unsemawang.consultation.constant;

public enum ConsultationMethod {
	TELEPHONE("전화상담"),
	VISIT("방문상담");

	private final String s;

	ConsultationMethod(String s) {
		this.s = s;
	}

	public String toString() {
		return name();
	}
}
