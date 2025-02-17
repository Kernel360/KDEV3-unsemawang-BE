package com.palbang.unsemawang.chat.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.interfaces.ChatRoomReader;
import com.palbang.unsemawang.chat.domain.repository.ChatRoomRepositoryCustom;
import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomReaderImpl implements ChatRoomReader {

	private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;

	@Override
	public Optional<ChatRoom> findById(Long chatRoomId) {
		return chatRoomRepositoryCustom.findById(chatRoomId);
	}

	@Override
	public Optional<ChatRoom> findByUser1AndUser2(String userId, String partnerId) {
		return chatRoomRepositoryCustom.findByUser1AndUser2(userId, partnerId);
	}

	@Override
	public String findOtherMemberIdInChatRoom(Long chatRoomId, String userId) {
		return chatRoomRepositoryCustom.findOtherMemberIdInChatRoom(chatRoomId, userId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));
	}

	@Override
	public List<ChatRoom> findByUserId(String userId) {
		return chatRoomRepositoryCustom.findByUserId(userId);
	}

	@Override
	public Map<Long, String> findOtherMemberIdsInChatRooms(Set<Long> chatRoomIds, String userId) {
		return chatRoomRepositoryCustom.findOtherMemberIdsInChatRooms(chatRoomIds, userId);
	}
}
