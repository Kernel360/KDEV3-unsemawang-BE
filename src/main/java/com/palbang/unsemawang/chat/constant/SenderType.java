package com.palbang.unsemawang.chat.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palbang.unsemawang.common.exception.GeneralException;

public enum SenderType {
	SELF("SELF", "본인"),
	OTHER("OTHER", "상대방");

	private String type;
	private String desc;

	SenderType(String type, String desc) {
		this.desc = desc;
		this.type = type;
	}

	@JsonValue
	public String getName() {
		return type;
	}

	@JsonCreator  // JSON → Enum 변환
	public static SenderType fromValue(String value) {
		for (SenderType type : SenderType.values()) {
			if (type.type.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new GeneralException();
	}

	@Override
	public String toString() {
		return this.type;
	}
}
