package com.palbang.unsemawang.activity.constant;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;

public enum ActiveStatus {
	ACTIVE_OTHERS("others", "활동중", true),
	ACTIVE_CHATROOM("chatRoom", "채팅중", true),
	ACTIVE_CHATROOM_LIST("chatRoomList", "채팅방 리스트 보는 중", true),
	INACTIVE("inactive", "비활성상태", false);

	private final String code;
	private final String name;
	private final Boolean isActive;

	ActiveStatus(String code, String name, boolean isActive) {
		this.code = code;
		this.name = name;
		this.isActive = isActive;
	}

	public boolean getIsActive() {
		return isActive;
	}

	@Override
	public String toString() {
		return code;
	}

	public static ActiveStatus getFromCode(String code) {
		for (ActiveStatus status : ActiveStatus.values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}

		throw new GeneralException(ResponseCode.NOT_SUPPORT_DATE_FORMAT);
	}
}
