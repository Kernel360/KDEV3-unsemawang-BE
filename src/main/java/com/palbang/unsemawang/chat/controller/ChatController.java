package com.palbang.unsemawang.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.chat.service.ChatMessageProducer;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Chat", description = "실시간 채팅 WebSocket API")
@Controller
@AllArgsConstructor
public class ChatController {

	private final ChatMessageProducer chatMessageProducer;
	private final ChatRoomRepository chatRoomRepository;

	@Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 메시지를 전송하고 RabbitMQ로 전달합니다.")
	@MessageMapping("/chat/sendMessage")
	public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
		if (chatMessageDto.getChatRoomId() == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
				"채팅방을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));

		boolean isReadOnly = (chatRoom.getUser1() == null || chatRoom.getUser2() == null);
		if (isReadOnly) {
			throw new GeneralException(ResponseCode.FORBIDDEN, "채팅방에서 메시지 입력이 차단되었습니다. 혼자 남은 상태입니다.");
		}

		try {
			chatMessageProducer.sendMessageToQueue(chatMessageDto);
		} catch (Exception e) {
			throw new GeneralException(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR, "메시지를 RabbitMQ로 전송하는 데 실패했습니다.", e);
		}
	}
}
