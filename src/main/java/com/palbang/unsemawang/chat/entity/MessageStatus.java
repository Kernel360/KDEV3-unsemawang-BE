package com.palbang.unsemawang.chat.entity;

public enum MessageStatus {
	SENT,       // 전송 완료
	DELIVERED,  // 소켓 서버에서 클라이언트로 전달됨
	READ        // 클라이언트에서 읽음
}