package com.palbang.unsemawang.chat.service;

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
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatMessageConsumer {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

	public ChatMessageConsumer(ChatMessageRepository chatMessageRepository,
		ChatRoomRepository chatRoomRepository,
		MemberRepository memberRepository,
		SimpMessagingTemplate messagingTemplate,
		ObjectMapper objectMapper) {
		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.memberRepository = memberRepository;
		this.messagingTemplate = messagingTemplate;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "chat.queue")
	@Transactional
	public void consumeMessage(String messageJson) {
		try {
			// ✅ JSON을 DTO로 변환
			ChatMessageDto chatMessageDto = objectMapper.readValue(messageJson, ChatMessageDto.class);
			log.info("📩 Received message from RabbitMQ: {}", chatMessageDto);

			// ✅ 필수 데이터 검증 (chatRoomId, senderId가 null인지 체크)
			if (chatMessageDto.getChatRoomId() == null || chatMessageDto.getSenderId() == null) {
				log.error("❌ Invalid ChatMessageDto: chatRoomId={}, senderId={}",
					chatMessageDto.getChatRoomId(), chatMessageDto.getSenderId());
				return;
			}

			// ✅ chatRoomId로 ChatRoom 조회
			ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
				.orElseThrow(() -> new IllegalStateException(
					"❌ ChatRoom을 찾을 수 없습니다. chatRoomId=" + chatMessageDto.getChatRoomId()));

			// ✅ senderId로 Member 조회
			Member sender = memberRepository.findById(chatMessageDto.getSenderId())
				.orElseThrow(() -> new IllegalStateException(
					"❌ Sender를 찾을 수 없습니다. senderId=" + chatMessageDto.getSenderId()));

			// ✅ timestamp가 없으면 현재 시간으로 설정
			Long timestamp = chatMessageDto.getTimestamp() != null
				? chatMessageDto.getTimestamp()
				: System.currentTimeMillis();

			// ✅ ChatMessage 엔티티로 변환
			ChatMessage chatMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.sender(sender)
				.content(chatMessageDto.getContent())
				.status(MessageStatus.RECEIVED) // ✅ 메시지를 "RECEIVED" 상태로 저장
				.timestamp(timestamp)
				.build();

			// ✅ 메시지 저장
			chatMessageRepository.save(chatMessage);
			log.info("✅ Chat message saved: {}", chatMessage);

			// ✅ WebSocket을 통해 클라이언트에게 전달할 DTO 생성
			ChatMessageDto responseMessage = ChatMessageDto.builder()
				.chatRoomId(chatRoom.getId())
				.senderId(sender.getId())
				.content(chatMessage.getContent())
				.status(chatMessage.getStatus())
				.timestamp(chatMessage.getTimestamp())
				.build();

			// ✅ WebSocket을 통해 사용자에게 메시지 전달
			messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), responseMessage);
			log.info("📩 Forwarded WebSocket message: {}", responseMessage);

		} catch (Exception e) {
			log.error("❌ 메시지 처리 실패", e);
		}
	}
}
