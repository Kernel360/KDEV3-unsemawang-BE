package com.palbang.unsemawang.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.chat.entity.MessageStatus;
import com.palbang.unsemawang.chat.service.ChatMessageService;

@RestController
@RequestMapping("/chat/messages")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@Autowired
	public ChatMessageController(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	@PatchMapping("/{id}/status")
	public void updateMessageStatus(
		@PathVariable Long id,
		@RequestParam MessageStatus status) {
		chatMessageService.updateMessageStatus(id, status);
	}
}