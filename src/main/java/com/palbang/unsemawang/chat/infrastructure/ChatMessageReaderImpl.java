package com.palbang.unsemawang.chat.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.interfaces.ChatMessageReader;
import com.palbang.unsemawang.chat.domain.repository.ChatMessageRepositoryCustom;
import com.palbang.unsemawang.chat.domain.repository.ChatRoomRepositoryCustom;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageReaderImpl implements ChatMessageReader {

	private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;
	private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;

	@Override
	public List<ChatMessage> findByChatRoomId(Long chatRoomId) {

		return chatMessageRepositoryCustom.findByChatRoomId(chatRoomId);
	}

	@Override
	public int countUnreadMessages(Long chatRoomId, String senderId) {

		String partnerId = chatRoomRepositoryCustom.findOtherMemberIdInChatRoom(chatRoomId, senderId)
			.orElseThrow(() -> new GeneralException(ResponseCode.RESOURCE_NOT_FOUND));

		return chatMessageRepositoryCustom.countUnreadMessages(chatRoomId, partnerId);
	}

	@Override
	public Map<Long, ChatMessage> findLatestMessagesForChatRooms(Set<Long> chatRoomIds) {
		return chatMessageRepositoryCustom.findLatestMessagesForChatRooms(chatRoomIds);
	}
}
