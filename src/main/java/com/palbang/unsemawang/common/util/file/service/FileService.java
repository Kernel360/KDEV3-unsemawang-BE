package com.palbang.unsemawang.common.util.file.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.util.file.dto.FileRequest;

public interface FileService {

	/**
	 * 이미지 업로드
	 * @param file 업로드 할 이미지 파일
	 * @param fileRequest ReferenceType과 ReferenceId 입력
	 */
	void uploadImage(MultipartFile file, FileRequest fileRequest);

	/**
	 * 이미지 업로드
	 * @param files 업로드 할 이미지 파일 리스트
	 * @param fileRequest ReferenceType과 ReferenceId 입력
	 */
	void uploadImagesAtOnce(List<MultipartFile> files, FileRequest fileRequest);

	/**
	 * 프로필 이미지 url 반환
	 * @param referenceId 참조된 회원 id, referenceId로 사용된 id
	 * @return 해당 회원의 프로필 이미지 or 디폴트 프로필 이미지
	 */
	String getProfileImgUrl(String referenceId);

	/**
	 * 게시글 대표 이미지 url 반환
	 * @param referenceId 참조된 게시글 id
	 * @return 해당 게시글의 대표 이미지
	 */
	String getPostThumbnailImgUrl(Long referenceId);

	/**
	 * 게시글 연관 이미지 url 리스트 반환
	 * @param referenceId 참조된 게시글 id
	 * @return 해당 게시글에 연관된 이미지 리스트
	 */
	List<String> getPostImgUrls(Long referenceId);

	/**
	 * 파일 url 리스트 반환
	 * @param fileRequest ReferenceType(참조 테이블명)과 ReferenceId(참조 id) 입력
	 * @return 파일 url 리스트
	 */
	List<String> getFileUrls(FileRequest fileRequest);

	/**
	 * 이미지 수정 : 이미지는 반드시 하나여야 함
	 * @param file 수정된 파일
	 * @param fileRequest 수정할 위치: ReferenceType, ReferenceId
	 */
	void updateImage(MultipartFile file, FileRequest fileRequest);

	/**
	 * 이미지 수정 : 여러 개의 이미지 중 하나를 수정
	 * @param file 수정된 파일
	 * @param updateTargetId 수정할 파일 id
	 */
	void updateImageById(MultipartFile file, Long updateTargetId);

	/**
	 * 이미지 수정 : 여러개의 이미지 전체 삭제 후 전체 추가
	 * @param files 이미지 리스트
	 * @param fileRequest 수정할 위치
	 */
	void updateImagesAtOnce(List<MultipartFile> files, FileRequest fileRequest);

	/**
	 * 파일 삭제
	 * @param fileRequest 삭제할 파일 위치:  ReferenceType, ReferenceId
	 * @return 삭제 여부
	 */
	boolean deleteFile(FileRequest fileRequest);

	/**
	 * 파일 삭제 여러개
	 * @param fileRequest 삭제할 파일 위치:  ReferenceType, ReferenceId
	 * @return 삭제 여부
	 */
	boolean deleteFiles(FileRequest fileRequest);

	/**
	 * 파일 삭제 아이디로
	 * @param fileId 삭제할 파일 아이디
	 * @return 삭제 여부
	 */
	boolean deleteFileById(Long fileId);

	/**
	 * 게시글 아이디로 연관된 이미지 소프트 삭제
	 * @param referenceId 삭제할 이미지의 게시글 아이디
	 * @return 삭제 여부
	 */
	boolean deletePostImgs(Long referenceId);
}
