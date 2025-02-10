package com.palbang.unsemawang.community.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.constants.ResponseCode;
import com.palbang.unsemawang.common.exception.GeneralException;
import com.palbang.unsemawang.common.util.file.constant.FileReferenceType;
import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.service.FileService;
import com.palbang.unsemawang.community.constant.CommunityCategory;
import com.palbang.unsemawang.community.dto.request.PostDeleteRequest;
import com.palbang.unsemawang.community.dto.request.PostRegisterRequest;
import com.palbang.unsemawang.community.dto.request.PostUpdateRequest;
import com.palbang.unsemawang.community.entity.Post;
import com.palbang.unsemawang.community.repository.PostRepository;
import com.palbang.unsemawang.member.entity.Member;
import com.palbang.unsemawang.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final FileService fileService;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Transactional(rollbackFor = Exception.class)
	public Post register(PostRegisterRequest postRegisterRequest) {

		// 0. 유효한 회원인지 확인
		Member member = memberRepository.findById(postRegisterRequest.getMemberId())
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 1. 게시글 등록
		Post post = Post.from(postRegisterRequest);
		post.updateMember(member);
		Post savedPost = postRepository.save(post);

		// 2. 저장이 안됐다면 예외 발생
		if (savedPost.getId() == null) {
			throw new GeneralException(ResponseCode.ERROR_INSERT, "게시글 등록에 실패했습니다");
		}

		return savedPost;
	}

	@Transactional(rollbackFor = Exception.class)
	public Post register(PostRegisterRequest postRegisterRequest, List<MultipartFile> fileList) {

		// 1. 게시글 등록
		Post savedPost = register(postRegisterRequest);

		// 2. 이미지 업로드
		fileService.uploadImagesAtOnce(fileList, FileRequest.of(FileReferenceType.COMMUNITY_BOARD, savedPost.getId()));

		return savedPost;
	}

	@Transactional(rollbackFor = Exception.class)
	public Post update(String memberId, Long postId, PostUpdateRequest postUpdateRequest) {

		// 0. 유효한 회원인지 확인
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 1. 회원 ID, 게시글 ID가 일치하는 게시글 조회
		Post post = postRepository.findByIdAndMember(postId, member)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_UNIQUE_NO, "유효하지 않는 게시글 입니다"));

		// 2. 게시글 업데이트
		post.updateFrom(postUpdateRequest);

		return post;
	}

	@Transactional(rollbackFor = Exception.class)
	public Post updatePostAndImgFiles(String memberId, Long postId, PostUpdateRequest postUpdateRequest,
		List<MultipartFile> fileList) {

		if (postUpdateRequest.getCategory() == CommunityCategory.SHARE_FORTUNE_BOARD) {
			throw new GeneralException(ResponseCode.NOT_UPDATED_SHARE_FORTUNE_CATEGORY);
		}

		Post updatedPost = update(memberId, postId, postUpdateRequest);

		// 이미지 업데이트(삭제 -> 추가)
		FileRequest fileRequest = FileRequest.of(FileReferenceType.COMMUNITY_BOARD, updatedPost.getId());
		fileService.deletePostImgs(postId);
		fileService.uploadImagesAtOnce(fileList, fileRequest);

		return updatedPost;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean delete(PostDeleteRequest postDeleteRequest) {
		// 1. 유효한 회원인지 확인
		Member member = memberRepository.findById(postDeleteRequest.getMemberId())
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_EXIST_MEMBER_ID));

		// 2. 회원, 게시글 id가 일치하되 삭제되지 않은 게시글 조회
		Post post = postRepository.findByMemberIsNotDeleted(postDeleteRequest.getPostId(), member)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_DELETE_AVAILABLE));

		// 3. 게시글 삭제 처리
		post.deletePost();

		// 4. 이미지 삭제 처리
		fileService.deletePostImgs(post.getId());

		return true;
	}

}
