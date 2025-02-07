package com.palbang.unsemawang.chat.constant;

public enum SenderType {
	SELF("본인"),
	OTHER("상대방");

	private String desc;

	SenderType(String desc) {
		this.desc = desc;
	}
}
