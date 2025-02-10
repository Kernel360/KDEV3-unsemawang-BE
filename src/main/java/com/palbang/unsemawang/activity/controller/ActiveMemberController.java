package com.palbang.unsemawang.activity.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palbang.unsemawang.activity.aop.NoTracking;
import com.palbang.unsemawang.activity.entity.ActiveMember;
import com.palbang.unsemawang.activity.service.ActiveMemberService;

import lombok.RequiredArgsConstructor;

// 최근 10분 내 접속중인 회원 확인용 api
@RestController
@RequiredArgsConstructor
@RequestMapping("/active-member")
public class ActiveMemberController {
	private final ActiveMemberService activeMemberService;

	@NoTracking
	@GetMapping
	public ResponseEntity<List<ActiveMember>> readActiveMemberList() {
		return ResponseEntity.ok(activeMemberService.findAllActiveMember());
	}
}
