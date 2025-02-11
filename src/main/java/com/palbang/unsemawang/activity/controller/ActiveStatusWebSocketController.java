package com.palbang.unsemawang.activity.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.activity.aop.NoTracking;
import com.palbang.unsemawang.activity.dto.ActiveMemberSaveRequest;
import com.palbang.unsemawang.activity.dto.websocket.ChangeActiveStatusMessage;
import com.palbang.unsemawang.activity.dto.websocket.NotifyActiveStatusMessage;
import com.palbang.unsemawang.activity.service.ActiveMemberRedisService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ActiveStatusWebSocketController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ActiveMemberRedisService activeMemberRedisService;

	@NoTracking
	@MessageMapping("/active/status/{memberId}")
	@SendTo("/topic/active/status/{memberId}")
	public NotifyActiveStatusMessage trackActivity(
		@DestinationVariable String memberId,
		ChangeActiveStatusMessage changeActiveStatusMessage
	) {

		// 회원 마지막 활동 시간 갱신
		ActiveMemberSaveRequest activeMemberSaveRequest = ActiveMemberSaveRequest.of(memberId,
			changeActiveStatusMessage);
		activeMemberRedisService.saveActiveMember(activeMemberSaveRequest);

		// 회원 상태 구독중인 사용자에게 전달
		boolean isActive = activeMemberSaveRequest.getStatus().getIsActive();
		return NotifyActiveStatusMessage.of(isActive);
	}
}
