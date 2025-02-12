package com.palbang.unsemawang.activity.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.activity.aop.NoTracking;
import com.palbang.unsemawang.activity.dto.ActiveMemberSaveRequest;
import com.palbang.unsemawang.activity.dto.websocket.ChangeActiveStatusMessage;
import com.palbang.unsemawang.activity.dto.websocket.NotifyActiveStatusMessage;
import com.palbang.unsemawang.activity.service.ActiveMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ActiveStatusWebSocketController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ActiveMemberService activeMemberService;

	@NoTracking
	@MessageMapping("/active/status")
	public void traceMemberActivity(
		ChangeActiveStatusMessage changeActiveStatusMessage,
		@Header("simpSessionAttributes") Map<String, Object> sessionAttributes
	) {

		String memberId = (String)sessionAttributes.get("userId");

		// 회원 마지막 활동 시간 갱신
		ActiveMemberSaveRequest activeMemberSaveRequest = ActiveMemberSaveRequest.of(memberId,
			changeActiveStatusMessage);
		activeMemberService.saveActiveMember(activeMemberSaveRequest);

		// 메세지 전달할 경로 설정
		String destination = "/topic/active/status/" + memberId;

		// 회원 상태 구독중인 사용자에게 전달
		boolean isActive = activeMemberSaveRequest.getStatus().getIsActive();
		NotifyActiveStatusMessage notifyActiveStatusMessage = NotifyActiveStatusMessage.of(isActive);
		simpMessageSendingOperations.convertAndSend(destination, notifyActiveStatusMessage);
	}

}
