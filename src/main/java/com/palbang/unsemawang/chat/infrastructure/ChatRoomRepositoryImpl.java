package com.palbang.unsemawang.chat.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.chat.domain.entity.ChatRoom;
import com.palbang.unsemawang.chat.domain.entity.QChatRoom;
import com.palbang.unsemawang.chat.domain.repository.ChatRoomRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;

	private final QChatRoom qChatRoom = QChatRoom.chatRoom;

	@Override
	public Optional<ChatRoom> findById(Long chatRoomId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(qChatRoom)
			.where(qChatRoom.id.eq(chatRoomId))
			.fetchOne());
	}

	@Override
	public Optional<ChatRoom> findByUser1AndUser2(String user1Id, String user2Id) {
		return Optional.ofNullable(queryFactory
			.selectFrom(qChatRoom)
			.where(qChatRoom.user1.id.eq(user1Id).and(qChatRoom.user2.id.eq(user2Id))
				.or(qChatRoom.user1.id.eq(user2Id).and(qChatRoom.user2.id.eq(user1Id))))
			.fetchFirst());
	}

	@Override
	public List<ChatRoom> findByUserId(String userId) {
		return queryFactory
			.selectFrom(qChatRoom)
			.where(qChatRoom.user1.id.eq(userId).or(qChatRoom.user2.id.eq(userId)))
			.fetch();
	}

	@Override
	public Optional<String> findOtherMemberIdInChatRoom(Long chatRoomId, String userId) {
		return Optional.ofNullable(queryFactory
			.select(
				new CaseBuilder()
					.when(qChatRoom.user1.id.eq(userId)).then(qChatRoom.user2.id)
					.otherwise(qChatRoom.user1.id)
			)
			.from(qChatRoom)
			.where(qChatRoom.id.eq(chatRoomId))
			.fetchOne());
	}

	@Override
	public ChatRoom save(ChatRoom chatRoom) {
		entityManager.persist(chatRoom);
		return chatRoom;
	}

	@Override
	public Map<Long, String> findOtherMemberIdsInChatRooms(Set<Long> chatRoomIds, String userId) {
		// 채팅방의 상대방 id를 구하는 식을 정의합니다.
		Expression<String> partnerIdExpression = new CaseBuilder()
			.when(qChatRoom.user1.id.eq(userId)).then(qChatRoom.user2.id)
			.otherwise(qChatRoom.user1.id);

		// 지정한 채팅방 id들에 대해 각 채팅방의 id와 상대방 id를 Tuple로 조회합니다.
		List<Tuple> results = queryFactory
			.select(qChatRoom.id, partnerIdExpression)
			.from(qChatRoom)
			.where(qChatRoom.id.in(chatRoomIds))
			.fetch();

		// 조회된 결과를 채팅방 id를 키, 상대방 id를 값으로 하는 Map으로 변환합니다.
		return results.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(qChatRoom.id),
				tuple -> tuple.get(partnerIdExpression)
			));
	}

}
