package com.palbang.unsemawang.chat.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.constant.MessageStatus;
import com.palbang.unsemawang.chat.domain.entity.ChatMessage;
import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.entity.QChatMessage;
import com.palbang.unsemawang.chat.domain.repository.ChatMessageRepositoryCustom;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;

	private final QChatMessage qChatMessage = QChatMessage.chatMessage;

	@Override
	public List<ChatMessage> findByChatRoomId(Long chatRoomId) {
		return queryFactory
			.selectFrom(qChatMessage)
			.where(qChatMessage.chatRoom.id.eq(chatRoomId))
			.orderBy(qChatMessage.timestamp.asc())
			.fetch();
	}

	@Override
	public int countUnreadMessages(Long chatRoomId, String senderId) {
		return Math.toIntExact(queryFactory
			.select(qChatMessage.count())
			.from(qChatMessage)
			.where(qChatMessage.chatRoom.id.eq(chatRoomId)
				.and(qChatMessage.sender.id.ne(senderId))
				.and(qChatMessage.status.eq(MessageStatus.RECEIVED)))
			.fetchOne());
	}

	@Override
	public void markMessagesAsRead(Long chatRoomId, String userId) {
		queryFactory.update(qChatMessage)
			.set(qChatMessage.status, MessageStatus.READ)
			.where(qChatMessage.chatRoom.id.eq(chatRoomId)
				.and(qChatMessage.sender.id.ne(userId)))
			.execute();
	}

	// 채팅방에서 가장 최근 메시지 찾기
	@Override
	public Optional<ChatMessage> findTopByChatRoomOrderByTimestampDesc(ChatRoom chatRoom) {
		return Optional.ofNullable(queryFactory
			.selectFrom(qChatMessage)
			.where(qChatMessage.chatRoom.eq(chatRoom))
			.orderBy(qChatMessage.timestamp.desc())
			.fetchFirst());
	}

	// 특정 채팅방에서 특정 유저가 보낸 메시지를 제외한 안 읽은 메시지 개수 조회
	@Override
	public int countByChatRoomAndSenderIdNotAndStatus(ChatRoom chatRoom, String senderId, MessageStatus status) {
		return Math.toIntExact(queryFactory
			.select(qChatMessage.count())
			.from(qChatMessage)
			.where(qChatMessage.chatRoom.eq(chatRoom)
				.and(qChatMessage.sender.id.ne(senderId))
				.and(qChatMessage.status.eq(status)))
			.fetchOne());
	}

	@Override
	public ChatMessage save(ChatMessage chatMessage) {
		entityManager.persist(chatMessage);
		return chatMessage;
	}

	@Override
	public List<ChatMessage> saveAll(List<ChatMessage> chatMessages) {
		for (ChatMessage chatMessage : chatMessages) {
			entityManager.persist(chatMessage);
		}
		return chatMessages;
	}

	@Override
	public Map<Long, ChatMessage> findLatestMessagesForChatRooms(Set<Long> chatRoomIds) {
		QChatMessage m1 = QChatMessage.chatMessage;
		QChatMessage m2 = new QChatMessage("m2");

		List<ChatMessage> latestMessages = queryFactory
			.select(m1)
			.from(m1)
			.where(m1.chatRoom.id.in(chatRoomIds)
				.and(m1.timestamp.eq(
					JPAExpressions.select(m2.timestamp.max())
						.from(m2)
						.where(m2.chatRoom.eq(m1.chatRoom))
				))
			)
			.fetch();

		return latestMessages.stream()
			.collect(Collectors.toMap(msg -> msg.getChatRoom().getId(), Function.identity()));
	}

}
