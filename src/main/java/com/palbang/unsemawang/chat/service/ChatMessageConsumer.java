package com.palbang.unsemawang.chat.service;

import java.time.ZoneId;

import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palbang.unsemawang.activity.service.ActiveMemberService;
import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.entity.ChatMessage;
import com.palbang.unsemawang.chat.repository.ChatMessageRepository;
import com.palbang.unsemawang.chat.repository.ChatRoomRepository;
import com.palbang.unsemawang.common.util.file.service.FileService;
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
	private final ActiveMemberService activeMemberService;

	@RabbitListener(queues = "chat.queue")
	@Transactional // 트랜잭션 적용
	public void consumeMessage(String messageJson) throws JsonProcessingException {
		// log.info("Received message from RabbitMQ: {}", messageJson);
		// ChatMessageDto chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto.class);
		//
		// Member sender = memberRepository.findById(chatMessageDto.getSenderId())
		// 	.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
		// 		"발신자를 찾을 수 없습니다. senderId=" + chatMessageDto.getSenderId()));
		//
		// ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
		// 	.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
		// 		"채팅방을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));
		//
		// Member chatPartner = chatRoom.getPartnerMember(sender.getId())
		// 	.orElseThrow(() -> new GeneralException(ResponseCode.DEFAULT_BAD_REQUEST));
		//
		// messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), chatMessageDto);
		// log.info("Forwarded WebSocket message: {}", chatMessageDto);
		//
		// // 새로운 메세지 내용과 안본 메세지 수를 보냄
		// String newMessageDestination = "/topic/chat/" + chatRoom.getId() + "/" + sender.getId() + "/new-message";
		// messagingTemplate.convertAndSend(newMessageDestination, NewChatMessageDto.of(chatMessageDto.getContent()));
		//
		// int count = chatMessageRepository.countByChatRoomAndSenderIdNotAndStatus(chatRoom, chatPartner.getId(),
		// 	MessageStatus.RECEIVED);
		// messagingTemplate.convertAndSend(newMessageDestination + "/count", NewChatMessageCountDto.of(count));

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
