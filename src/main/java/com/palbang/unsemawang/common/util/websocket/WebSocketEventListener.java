package com.palbang.unsemawang.common.util.websocket;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.palbang.unsemawang.activity.constant.ActiveStatus;
import com.palbang.unsemawang.activity.dto.ActiveMemberSaveRequest;
import com.palbang.unsemawang.activity.dto.websocket.NotifyActiveStatusMessage;
import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.service.ActiveMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 텍스트 기반 Websocket 메세지 처리를 수행하는 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
	private final ActiveMemberService activeMemberService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 웹소켓 연결 이벤트 핸들러
	 * @param event
	 */
	@EventListener
	public void handleSessionConnect(SessionConnectEvent event) {
		// 회원 ID 추출
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = (String)getSessionValue(headerAccessor, "userId");

		if (userId == null) {
			log.error("인증되지 않은 사용자의 접근입니다");
			return;
		}

		log.info("웹소켓 연결된 사용자 ID :" + userId);

		// 활동 상태 저장
		ActiveMemberSaveRequest saveRequest = ActiveMemberSaveRequest.builder()
			.memberId(userId)
			.status(ActiveStatus.ACTIVE_OTHERS)
			.build();
		activeMemberService.saveActiveMember(saveRequest);

		// 활동 상태 공유
		String destination = "/topic/active/status/" + userId;
		simpMessagingTemplate.convertAndSend(destination, NotifyActiveStatusMessage.of(true));
	}

	/**
	 * 웹소켓 채널 구독 이벤트 핸들러
	 * @param event
	 */
	@EventListener
	public void handleSessionSubscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		// 구독한 채널 정보 가져오기
		String destination = headerAccessor.getDestination();
		log.info("received session subscribe: {}", destination);

		// "/topic/active/status/{memberId}" 에 대한 구독만 처리
		if (destination != null && destination.startsWith("/topic/active/status/")) {
			String memberId = destination.split("/")[4];
			log.info("조회하고자 하는 member ID: {}", memberId);

			// 활동 상태 공유
			ActiveMember activeMember = activeMemberService.findActiveMemberById(memberId);
			simpMessagingTemplate.convertAndSend(destination, NotifyActiveStatusMessage.of(activeMember.getStatus()
				.getIsActive()));
		}
	}

	/**
	 * 웹소켓 연결 해제 핸들러
	 * @param event
	 */
	@EventListener
	public void handleSessionDisconnect(SessionDisconnectEvent event) {
		// 회원 ID 추출
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userId = (String)getSessionValue(headerAccessor, "userId");

		if (userId == null) {
			log.error("인증되지 않은 사용자의 접근입니다");
			return;
		}

		// 활동 상태 공유
		String destination = "/topic/active/status/" + userId;
		simpMessagingTemplate.convertAndSend(destination, NotifyActiveStatusMessage.of(false));

		// 활동 상태 저장
		ActiveMemberSaveRequest saveRequest = ActiveMemberSaveRequest.builder()
			.memberId(userId)
			.status(ActiveStatus.INACTIVE)
			.build();
		activeMemberService.saveActiveMember(saveRequest);
	}

	private Object getSessionValue(StompHeaderAccessor headerAccessor, String key) {

		Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
		log.info("session attributes:" + sessionAttributes);

		if (sessionAttributes != null) {
			return sessionAttributes.get(key);
		}

		return null;
	}

}
