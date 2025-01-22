package com.palbang.unsemawang.community.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.constant.MemberRole;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

@SpringBootTest(classes = PostService.class)
class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private PostRepository postRepository;

	@MockBean
	private MemberRepository memberRepository;

	@MockBean
	private FileService fileService;

	@Test
	@DisplayName(value = "게시글 등록 - 모든 값이 정상적으로 들어온 경우")
	public void postRegisterTest() {
		// given

		Member member = createMember();

		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.title("test-title")
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.memberId(member.getId())
			.build();

		Post post = Post.builder()
			.id(1L)
			.communityCategory(postRegisterRequest.getCategory())
			.title(postRegisterRequest.getTitle())
			.content(postRegisterRequest.getContent())
			.member(member)
			.build();

		// when
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
		when(postRepository.save(any(Post.class))).thenReturn(post);

		// then
		Post registeredPost = postService.register(postRegisterRequest);
		assertNotNull(registeredPost.getId());

		verify(postRepository, times(1)).save(any());
	}

	@Test
	@DisplayName(value = "게시글 등록 - 저장이 안됐을 경우")
	public void postRegisterTest_saveFailure() {
		// given
		Member member = createMember();
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.memberId(member.getId())
			.build();

		Post post = Post.builder()
			.communityCategory(postRegisterRequest.getCategory())
			.title(postRegisterRequest.getTitle())
			.content(postRegisterRequest.getContent())
			.build();

		// when
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
		when(postRepository.save(any(Post.class))).thenReturn(post);

		// then
		assertThrows(GeneralException.class, () -> postService.register(postRegisterRequest));

		verify(postRepository, times(1)).save(any());
	}

	@Test
	@DisplayName(value = "게시글 등록 - 존재하지 않는 회원일 경우")
	public void postRegisterTest_notExistMember() {
		// given
		String memberId = "not-exist-member";
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
			.title("test-title")
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.memberId(memberId)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
		assertThrows(GeneralException.class, () -> postService.register(postRegisterRequest));

		verify(memberRepository, times(1)).findById(memberId);
		verify(postRepository, times(0)).save(any());
	}

	@Test
	@DisplayName(value = "게시글 수정 - 회원의 게시글이 아닐 경우")
	public void postUpdateTest_notPostOfMember() {
		// given - 회원 엔티티, 게시글 수정 DTO 객체 생성
		Member member = createMember();
		Long postId = 100L;
		PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
			.title("test-title")
			.category(CommunityCategory.FREE_BOARD)
			.content("test-content")
			.build();

		// when, then - 회원이 작성한 게시글이 아닐 경우, GeneralException이 발생한다
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
		when(postRepository.findByIdAndMember(postId, member)).thenReturn(Optional.empty());
		assertThrows(GeneralException.class, () -> postService.update(member.getId(), postId, postUpdateRequest));

		verify(memberRepository, times(1)).findById(member.getId());
		verify(postRepository, times(1)).findByIdAndMember(postId, member);
	}

	/* 헬퍼 */
	private Member createMember() {
		return Member.builder()
			.id("test-user")
			.role(MemberRole.GENERAL)
			.email("test@unsemawang.com")
			.isJoin(true)
			.build();
	}

}