package com.palbang.unsemawang.chat.constant;

public enum SendType {
	SELF("본인"),
	OTHER("상대방");

	private String desc;

	SendType(String desc) {
		this.desc = desc;
	}
}
