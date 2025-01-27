package com.palbang.unsemawang.common.util.file.dto;

import com.palbang.unsemawang.common.util.file.constant.FileReferenceType;

/**
 * 해당 파일이 필요한 곳의 정보로 파일을 요청
 * of 메서드 사용해주세요
 * @param referenceType 참조할 테이블명_파일유형 e.g FileReferenceType.MEMBER_PROFILE_IMAGE
 * @param referenceId 참조할 컬럼 ID (Long)
 * @param referenceStringId 참조할 컬럼 ID (String)
 */
public record FileRequest(
	FileReferenceType referenceType,
	Long referenceId,
	String referenceStringId
) {
	public static FileRequest of(FileReferenceType referenceType, Long referenceId) {
		return new FileRequest(referenceType, referenceId, null);
	}

	public static FileRequest of(FileReferenceType referenceType, String referenceStringId) {
		return new FileRequest(referenceType, null, referenceStringId);
	}
}
