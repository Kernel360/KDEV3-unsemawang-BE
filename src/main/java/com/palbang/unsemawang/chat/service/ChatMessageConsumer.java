package com.palbang.unsemawang.chat.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.entity.ChatRoom;
import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ChatMessageConsumer {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	private final FileService fileService;

	@RabbitListener(queues = "chat.queue")
	@Transactional // 트랜잭션 적용
	public void consumeMessage(String messageJson) {
		try {
			log.info("Received message from RabbitMQ: {}", messageJson);
			ChatMessageDto chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto.class);

			Member sender = memberRepository.findById(chatMessageDto.getSenderId())
				.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
					"발신자를 찾을 수 없습니다. senderId=" + chatMessageDto.getSenderId()));

			ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
				.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
					"채팅방을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));

			ChatMessage chatMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.sender(sender)
				.content(chatMessageDto.getContent())
				.status(MessageStatus.RECEIVED)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(chatMessageDto.getTimestamp()),
					ZoneId.systemDefault()))
				.build();

			chatMessageRepository.save(chatMessage);

			ChatMessageDto responseMessage = convertToDto(chatMessage);
			messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), responseMessage);
			log.info("Forwarded WebSocket message: {}", responseMessage);

		} catch (Exception e) {
			log.error("메시지 처리 실패: {}", e.getMessage(), e);
			throw new GeneralException(ResponseCode.DEFAULT_INTERNAL_SERVER_ERROR, "채팅 메시지 처리 중 예상치 못한 오류가 발생했습니다.");
		}
	}

	// Lazy Loading 해결 후 DTO 변환
	private ChatMessageDto convertToDto(ChatMessage message) {
		Hibernate.initialize(message.getSender());

		// 프로필 이미지 URL 가져오기
		String profileImageUrl = fileService.getProfileImgUrl(message.getSender().getId());

		return ChatMessageDto.builder()
			.chatRoomId(message.getChatRoom().getId())
			.senderId(message.getSender().getId())
			.nickname(message.getSender().getNickname())
			.profileImageUrl(profileImageUrl) // 백엔드에서도 NULL 방지
			.content(message.getContent())
			.timestamp(message.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
			.status(message.getStatus())
			.build();
	}
}
