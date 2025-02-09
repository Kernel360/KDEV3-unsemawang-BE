package com.palbang.unsemawang.chat.constant;

public enum SenderType {
	SELF("self", "본인"),
	OTHER("other", "상대방");

	private String type;
	private String desc;

	SenderType(String type, String desc) {
		this.desc = desc;
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
