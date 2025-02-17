package com.palbang.unsemawang.activity.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.activity.aop.NoTracking;
import com.palbang.unsemawang.activity.dto.ActiveMemberSaveRequest;
import com.palbang.unsemawang.activity.dto.websocket.ChangeActiveStatusMessage;
import com.palbang.unsemawang.activity.dto.websocket.NotifyActiveStatusMessage;
import com.palbang.unsemawang.activity.service.ActiveMemberService;
import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ActiveStatusWebSocketController {
	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ActiveMemberService activeMemberService;

	/**
	 * 활동 상태 변경 요청 처리
	 * @param changeActiveStatusMessage
	 * @param stompHeaderAccessor
	 */
	@NoTracking
	@MessageMapping("/active/status")
	@SendTo("/topic/active/status")
	public void traceMemberActivity(
		ChangeActiveStatusMessage changeActiveStatusMessage,
		StompHeaderAccessor stompHeaderAccessor
	) {

		String memberId = getSessionUserId(stompHeaderAccessor);

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

	private String getSessionUserId(StompHeaderAccessor stompHeaderAccessor) {

		// 회원 ID 추출
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)stompHeaderAccessor.getUser();

		if (auth == null || auth.getPrincipal() == null) {
			return null;
		}

		return ((CustomOAuth2User)auth.getPrincipal()).getId();

	}

}
