package com.palbang.unsemawang.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ChatMessageProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final FileService fileService;

	public void sendMessageToQueue(ChatMessage chatMessage) {
		try {
			if (chatMessage.getChatRoom() == null) {
				log.error("chatRoomId가 없는 메시지는 전송 불가: {}", chatMessage);
				throw new GeneralException(ResponseCode.ERROR_REQUEST, "chatRoomId가 누락되었습니다.");
			}

			// LocalDateTime이 null이면 현재 시간을 설정
			if (chatMessage.getTimestamp() == null) {
				chatMessage.setTimestamp(LocalDateTime.now());  // long 유지
			}

			String memberId = chatMessage.getSender().getId();
			String profileImageUrl = fileService.getProfileImgUrl(memberId);

			ChatMessageDto chatMessageDto = ChatMessageDto.builder()
				.chatRoomId(chatMessage.getChatRoom().getId())
				.senderId(chatMessage.getSender().getId())
				.content(chatMessage.getContent())
				.status(chatMessage.getStatus())
				// 로컬 데이트 타임을 밀리초(Long)로 변환 // 메세지큐 담을때 로컬데이트타임 담으면 에러난다고 합니다. (확인은 안해봤습니다.)
				.timestamp(chatMessage.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
				.profileImageUrl(profileImageUrl)
				.nickname(chatMessage.getSender().getNickname())
				.build();

			String messageJson = objectMapper.writeValueAsString(chatMessageDto);
			log.info("Sending message to RabbitMQ: {}", messageJson);
			rabbitTemplate.convertAndSend("chat.exchange", "chat.routing.key", messageJson);
		} catch (Exception e) {
			log.error("메시지 전송 중 오류 발생", e);
			throw new GeneralException(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR, "메시지 전송에 실패했습니다.", e);
		}
	}
}
