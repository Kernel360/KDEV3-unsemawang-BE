package com.palbang.unsemawang.activity.dto;

import com.palbang.unsemawang.activity.constant.ActiveStatus;
import com.palbang.unsemawang.activity.dto.websocket.ChangeActiveStatusMessage;
import com.palbang.unsemawang.activity.entity.ActiveMember;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveMemberSaveRequest {
	private String memberId;

	private ActiveStatus status;

	private Long chatRoomId;

	public static ActiveMemberSaveRequest of(String memberId, ChangeActiveStatusMessage changeActiveStatusMessage) {
		return ActiveMemberSaveRequest.builder()
			.memberId(memberId)
			.status(ActiveStatus.getFromCode(changeActiveStatusMessage.getWhere()))
			.chatRoomId(changeActiveStatusMessage.getChatRoomId())
			.build();
	}

	public ActiveMember toActiveMemberEntity() {
		return ActiveMember.builder()
			.memberId(memberId)
			.status(status)
			.chatRoomId(chatRoomId)
			.build();
	}
}
