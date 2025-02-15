package com.palbang.unsemawang.chat.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.palbang.unsemawang.chat.dto.ChatMessageDto;
import com.palbang.unsemawang.chat.dto.request.ChatMessageRequest;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final FileService fileService;

	public ChatMessageDto saveChatMessage(String memberId, Long chatRoomId, ChatMessageRequest chatMessageRequest) {
		if (chatRoomId == null) {
			throw new GeneralException(ResponseCode.EMPTY_PARAM_BLANK_OR_NULL, "chatRoomId가 누락되었습니다.");
		}

		Member sender = memberRepository.findById(memberId)
			.orElseThrow(
				() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND, "발신자를 찾을 수 없습니다. senderId=" + memberId));

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND,
				"채팅방을 찾을 수 없습니다. chatRoomId=" + chatRoomId));

		if (chatRoom.getUser1() == null || chatRoom.getUser2() == null) {
			throw new GeneralException(ResponseCode.FORBIDDEN, "채팅방에서 메시지 입력이 차단되었습니다. 혼자 남은 상태입니다.");
		}

		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.content(chatMessageRequest.getMessage())
			.status(
				MessageStatus.RECEIVED)
			.timestamp(LocalDateTime.now())
			.build();

		chatMessageRepository.save(chatMessage);

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

		return chatMessageDto;
	}

	public int getNotReadMessageOfPartner(String memberId, Long chatRoomId) {
		String partnerId = chatRoomRepository.findOtherMemberIdInChatRoom(chatRoomId, memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		return chatMessageRepository.countByChatRoomIdAndSenderIdNotAndStatus(
			chatRoomId, partnerId, MessageStatus.RECEIVED
		);
	}
}
