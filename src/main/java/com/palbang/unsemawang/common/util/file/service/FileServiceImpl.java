package com.palbang.unsemawang.common.util.file.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.util.file.constant.FileReferenceType;
import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.entity.File;
import com.palbang.unsemawang.common.util.file.exception.FileDeleteException;
import com.palbang.unsemawang.common.util.file.exception.FileNotFoundException;
import com.palbang.unsemawang.common.util.file.exception.FileSizeExceededException;
import com.palbang.unsemawang.common.util.file.exception.FileUploadException;
import com.palbang.unsemawang.common.util.file.exception.InvalidFileFormatException;
import com.palbang.unsemawang.common.util.file.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileServiceImpl implements FileService {

	/* S3Service는 @Transactional 불가, 수동으로 일관성을 관리해줘야 함 */
	final FileRepository fileRepository;
	final S3Service s3Service;

	@Override
	public void uploadImage(MultipartFile file, FileRequest fileRequest) {

		validFileSize(file);
		validImageFileExtension(file);

		String path = s3Service.upload(file, fileRequest);
		File fileEntity = buildFileEntity(file, path, fileRequest);
		saveFileOrRollback(fileEntity, path, fileRequest);
	}

	@Override
	public void uploadImagesAtOnce(List<MultipartFile> files, FileRequest fileRequest) {
		if (files == null || files.isEmpty()) {
			log.warn("파일이 없습니다. 파일 참조 정보: {}", fileRequest);
			return;
		}

		files.forEach(f -> uploadImage(f, fileRequest));
	}

	/*

	upload 업로드
	------------------------------------------------------------------------------------------------
	download 다운로드

	 */
	@Override
	public String getProfileImgUrl(String referenceId) {

		FileRequest fileRequest = FileRequest.of(FileReferenceType.MEMBER_PROFILE_IMG, referenceId);

		String key;
		List<File> files = fileRepository.findFilesByFileReference(fileRequest);
		if (files.isEmpty()) {
			key = "MEMBER_PROFILE_IMG/default_profile.png";
		} else {
			key = files.get(0).getS3Key();
		}

		if (files.size() > 1) {
			log.warn("파일이 한 개가 아닙니다. 파일 참조 정보: {}", fileRequest);
		}
		return s3Service.createPresignedGetUrl(key);
	}

	@Override
	public String getAnonymousProfileImgUrl() {

		String key;

		key = "MEMBER_PROFILE_IMG/anoymous_profile_image.webp";

		return s3Service.createPresignedGetUrl(key);
	}

	@Override
	public String getPostThumbnailImgUrl(Long referenceId) {
		FileRequest fileRequest = FileRequest.of(FileReferenceType.COMMUNITY_BOARD, referenceId);

		List<File> files = fileRepository.findFilesByFileReferenceAndIsNotDeleted(fileRequest);
		if (files.isEmpty()) {
			log.warn("파일이 없습니다. 파일 참조 정보: {}", fileRequest);
			return null;
		}

		return s3Service.createPresignedGetUrl(files.get(0).getS3Key());
	}

	public List<String> getPostImgUrls(Long referenceId) {
		FileRequest fileRequest = FileRequest.of(FileReferenceType.COMMUNITY_BOARD, referenceId);

		List<File> files = fileRepository.findFilesByFileReferenceAndIsNotDeleted(fileRequest);
		if (files.isEmpty()) {
			log.warn("파일이 없습니다. 파일 참조 정보: {}", fileRequest);
			return List.of();
		}

		List<String> paths = new ArrayList<>(files.size());
		for (File f : files) {
			paths.add(s3Service.createPresignedGetUrl(f.getS3Key()));
		}
		return paths;
	}

	@Override
	public String getContentThumbnailImgUrl(Long referenceId) {
		FileRequest fileRequest = FileRequest.of(FileReferenceType.CONTENT_THUMBNAIL_IMG, referenceId);

		List<File> files = fileRepository.findFilesByFileReferenceAndIsNotDeleted(fileRequest);
		if (files.isEmpty()) {
			log.warn("파일이 없습니다. 파일 참조 정보: {}", fileRequest);
			return null;
		}

		return s3Service.createPresignedGetUrl(files.get(0).getS3Key());
	}

	@Override
	public List<String> getFileUrls(FileRequest fileRequest) {

		List<File> files = fileRepository.findFilesByFileReference(fileRequest);
		validFilesNotEmpty(files, fileRequest);

		List<String> paths = new ArrayList<>(files.size());
		for (File f : files) {
			paths.add(s3Service.createPresignedGetUrl(f.getS3Key()));
		}
		return paths;
	}

  /*
  download
  ------------------------------------------------------------------------------
  update
   */

	@Override
	public void updateImage(MultipartFile file, FileRequest fileRequest) {

		boolean isFileNull = isNullableFileNull(fileRequest);

		if (isFileNull) {
			uploadImage(file, fileRequest);

		} else {
			validFileSize(file);
			validImageFileExtension(file);

			String key = fileRepository.findFilesByFileReference(fileRequest).get(0).getS3Key();

			String path = s3Service.updateObject(file, key);
			File fileEntity = buildUpdatedFileEntity(file, path, fileRequest);
			saveFileOrRollback(fileEntity, path, fileRequest);
		}
	}

	@Override
	public void updateImageById(MultipartFile file, Long updateTargetId) {

		validFileSize(file);
		validImageFileExtension(file);
		updateFile(file, updateTargetId);
	}

	/**
	 * 파일 업데이트 로직
	 *
	 * @param file     업로드할 파일
	 * @param targetId 업데이트할 파일 ID
	 */
	private void updateFile(MultipartFile file, Long targetId) {

		Optional<File> existing = fileRepository.findById(targetId);
		if (existing.isEmpty()) {
			log.error("파일을 찾을 수 없습니다. 업데이트하려는 파일 ID: {}", targetId);
			throw new FileNotFoundException("업데이트 할 파일을 찾을 수 없습니다.");
		}

		fileRepository.save(File.builder()
			.id(targetId)
			.size(file.getSize())
			.type(file.getContentType())
			.build());
		try {
			s3Service.updateObject(file, existing.get().getS3Key());
		} catch (Exception e) {
			fileRepository.save(existing.get());
			throw new FileUploadException();
		}
	}

	@Override
	public void updateImagesAtOnce(List<MultipartFile> file, FileRequest fileRequest) {

		List<File> files = fileRepository.findFilesByFileReference(fileRequest);

		validFilesNotEmpty(files, fileRequest);

	}

  /*
  update
  ------------------------------------------------------------------------------
  delete
  */

	@Override
	public boolean deleteFile(FileRequest fileRequest) {

		List<File> files = fileRepository.findFilesByFileReference(fileRequest);
		if (files.isEmpty()) {
			log.error("삭제하려는 파일을 찾을 수 없습니다. 파일 참조 정보: {}", fileRequest);
			throw new FileNotFoundException("삭제할 파일을 찾을 수 없습니다.");
		}

		try {
			fileRepository.delete(files.get(0));
		} catch (DataAccessException e) {
			log.error("파일 삭제 실패 - DB 오류 발생. 파일 참조 정보: {}", fileRequest, e);
			throw new FileDeleteException();
		} catch (Exception e) {
			log.error("파일 삭제 실패. 파일 ID: {}, 파일 경로: {}", files.get(0).getId(), files.get(0).getS3Key(), e);
			throw new FileDeleteException();
		}

		try {
			return s3Service.deleteObject(files.get(0).getS3Key());
		} catch (Exception e) {
			fileRepository.save(files.get(0));
			throw new FileDeleteException();
		}
	}

	@Override
	public boolean deleteFiles(FileRequest fileRequest) {

		List<File> files = fileRepository.findFilesByFileReference(fileRequest);

		validFilesNotEmpty(files, fileRequest);

		try {
			for (File file : files) {
				fileRepository.delete(file);
				s3Service.deleteObject(file.getS3Key());
			}
			fileRepository.flush();
		} catch (Exception e) {
			log.error("파일 삭제 실패. 파일 참조 정보: {}", fileRequest, e);
			fileRepository.saveAll(files);
			throw new FileDeleteException();
		}

		return true;
	}

	@Override
	public boolean deleteFileById(Long fileId) {
		Optional<File> file = fileRepository.findById(fileId);
		if (file.isEmpty()) {
			log.error("삭제하려는 파일을 찾을 수 없습니다. 파일 id: {}", fileId);
			throw new FileNotFoundException("삭제할 파일을 찾을 수 없습니다.");
		}

		fileRepository.delete(file.get());

		try {
			s3Service.deleteObject(file.get().getS3Key());
		} catch (Exception e) {
			fileRepository.save(file.get());
			throw new FileDeleteException();
		}
		return true;
	}

	@Override
	public boolean deletePostImgs(Long referenceId) {
		FileRequest fileRequest = FileRequest.of(FileReferenceType.COMMUNITY_BOARD, referenceId);
		List<File> files = fileRepository.findFilesByFileReferenceAndIsNotDeleted(fileRequest);

		for (File f : files) {
			f.softDelete();
		}
		fileRepository.flush();

		return false;
	}

/*
  delete
  ------------------------------------------------------------------------------
  custom common private methods
  */

	/**
	 * 새로 저장할 파일 엔티티를 생성 후 반환
	 * @param file
	 * @param s3KeyName
	 * @param fileRequest
	 * @return
	 */
	private File buildFileEntity(MultipartFile file, String s3KeyName, FileRequest fileRequest) {
		return File.builder()
			.s3Key(s3KeyName)
			.type(file.getContentType())
			.size(file.getSize())
			.referenceType(fileRequest.referenceType())
			.referenceId(fileRequest.referenceId())
			.referenceStringId(fileRequest.referenceStringId())
			.build();
	}

	/**
	 * 업데이트 할 파일 엔티티를 찾아서 수정 후 반환
	 * @param file
	 * @param s3KeyName
	 * @param fileRequest
	 * @return
	 */
	private File buildUpdatedFileEntity(MultipartFile file, String s3KeyName, FileRequest fileRequest) {

		List<File> existingEntities = fileRepository.findFilesByFileReference(fileRequest);

		validFilesNotEmpty(existingEntities, fileRequest);

		return File.builder()
			.id(existingEntities.get(0).getId())
			.s3Key(s3KeyName)
			.type(file.getContentType())
			.size(file.getSize())
			.referenceType(fileRequest.referenceType())
			.referenceId(fileRequest.referenceId())
			.referenceStringId(fileRequest.referenceStringId())
			.build();
	}

	/**
	 * s3 파일 저장 이후 사용
	 * 파일 정보를 db에 저장하고 실패 시 s3에서 삭제
	 * @param fileEntity
	 * @param s3KeyName
	 * @param fileRequest
	 */
	private void saveFileOrRollback(File fileEntity, String s3KeyName, FileRequest fileRequest) {
		try {
			fileRepository.save(fileEntity);
		} catch (DataAccessException e) {
			s3Service.deleteObject(s3KeyName);
			log.error("파일 정보 DB 저장 실패. 참조 ID: {}, 참조 타입: {}.",
				fileRequest.referenceId(), fileRequest.referenceType(), e);
			throw new FileUploadException("파일 정보 저장 중 오류가 발생했습니다.");
		} catch (Exception e) {
			s3Service.deleteObject(s3KeyName);
			log.error("파일 엔티티 저장 실패. 참조 ID: {}, 참조 타입: {}.",
				fileRequest.referenceId(), fileRequest.referenceType(), e);
			throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.");
		}
	}

	/**
	 * 파일 존재, 크기 체크
	 *
	 * @param file 체크할 파일
	 */
	private void validFileSize(MultipartFile file) {
		long fileMaxSize = 5 * 1024 * 1024;
		if (file.getSize() > fileMaxSize) {
			log.error("파일 크기가 너무 큽니다. 파일 이름: {}, 크기: {}MB, 최대 크기: {}MB",
				file.getOriginalFilename(), file.getSize() / 1024 / 1024, fileMaxSize / 1024 / 1024);
			throw new FileSizeExceededException("파일 크기가 너무 큽니다. 최대 크기는 " + fileMaxSize / 1024 / 1024 + "MB입니다.");
		}
	}

	private boolean isNullableFileNull(FileRequest fileRequest) {

		List<File> files = fileRepository.findFilesByFileReference(fileRequest);

		return files.isEmpty();
	}

	private void validFilesNotEmpty(List<File> files, FileRequest fileRequest) {
		if (files.isEmpty()) {
			log.error("파일이 비어 있습니다. 파일 참조 정보: {}", fileRequest);
			throw new InvalidFileFormatException("파일이 비어 있습니다.");
		}
	}

	/**
	 * 파일의 타입을 검사하는 메서드
	 * @param file 검사할 파일
	 * @throws InvalidFileFormatException 파일 형식이 맞지 않으면 예외 발생
	 */
	private void validImageFileExtension(MultipartFile file) {

		List<String> allowedContentTypes = List.of("jpeg", "png", "webp", "heic", "gif");

		String contentType = file.getContentType();
		if (contentType == null || contentType.isEmpty()) {
			log.error("파일의 Content-Type이 비어있거나 null입니다. 파일 이름: {}", file.getOriginalFilename());
			throw new InvalidFileFormatException("Content-Type이 누락되었습니다.");
		}

		for (String allowedContentType : allowedContentTypes) {
			if (contentType.contains(allowedContentType)) {
				return;
			}
		}

		log.error("파일 형식 check fail : 파일 이름: {}, 파일 타입: {}", file.getOriginalFilename(), file.getContentType());
		throw new InvalidFileFormatException("지원되는 파일 형식을 확인해 주세요.");
	}

}
