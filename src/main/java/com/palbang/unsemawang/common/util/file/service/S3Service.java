package com.palbang.unsemawang.common.util.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.exception.FileDeleteException;
import com.palbang.unsemawang.common.util.file.exception.FileUploadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	@Value("${cloud.aws.region.static}")
	private Region region;

	private static String createStructuredReferenceId(FileRequest fileRequest) {

		if (fileRequest.referenceStringId() == null) {

			return String.format("%04d/", fileRequest.referenceId())
				+ "/" + UUID.randomUUID();
		} else {

			return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000))
				+ "/" + fileRequest.referenceStringId();
		}
	}

	/**
	 * S3 버킷에 업로드
	 * @param file 업로드 할 파일
	 * @param fileRequest 폴더 스트럭쳐를 만들기 위한 reference 정보
	 * @return 업로드 한 경로, s3의 keyName
	 */
	public String upload(MultipartFile file, FileRequest fileRequest) {

		String keyName = fileRequest.referenceType() + "/" + createStructuredReferenceId(fileRequest);

		try (InputStream inputStream = file.getInputStream()) {
			RequestBody requestBody = RequestBody.fromInputStream(inputStream, file.getSize());

			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucketName)
					.key(keyName)
					.contentType(file.getContentType())
					.contentLength(file.getSize())
					.build(),
				requestBody);

		} catch (IOException e) {
			log.error("S3 파일 업로드에 실패했습니다. 파일 입력 오류.  fileRequest 정보: referenceId={}, referenceType={}. 오류 메시지: {}",
				fileRequest.referenceId(), fileRequest.referenceType(), e.getMessage(), e);
			throw new FileUploadException();
		} catch (SdkException e) {
			log.error("S3 파일 업로드에 실패했습니다. AWS SDK 오류.  fileRequest 정보: referenceId={}, referenceType={}. 오류 메시지: {}",
				fileRequest.referenceId(), fileRequest.referenceType(), e.getMessage(), e);
			throw new FileUploadException();
		} catch (Exception e) {
			log.error("S3 파일 업로드에 실패했습니다. fileRequest 정보: referenceId={}, referenceType={}. 오류 메시지: {}",
				fileRequest.referenceId(), fileRequest.referenceType(), e.getMessage(), e);
			throw new FileUploadException();
		}

		return keyName;
	}

	// /**
	//  * Create a pre-signed URL to download an object in a subsequent GET request
	//  * @param keyName S3에 저장된 key
	//  * @return presignedGetURL
	//  */
	// public String createPresignedGetUrl(String keyName) {
	// 	GetObjectRequest objectRequest = GetObjectRequest.builder()
	// 		.bucket(bucketName)
	// 		.key(keyName)
	// 		.build();
	//
	// 	GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
	// 		.signatureDuration(Duration.ofHours(1))  // URL duration
	// 		.getObjectRequest(objectRequest)
	// 		.build();
	//
	// 	PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
	//
	// 	return presignedRequest.url().toExternalForm();
	// }

	public String generateObjectUrl(String keyName) {

		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
	}

	/**
	 * 버킷에 있는 파일 삭제
	 * @param keyName S3에 저장된 key
	 * @return 삭제 true/false
	 */
	public boolean deleteObject(String keyName) {

		try {
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.build();

			DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);

			return response.sdkHttpResponse().isSuccessful();
		} catch (SdkException e) {
			log.error("S3 파일 삭제에 실패했습니다. AWS SDK 오류. 키 이름: {} 오류 메시지: {}", keyName, e.getMessage(), e);
			throw new FileDeleteException();
		} catch (Exception e) {
			log.error("S3 파일 삭제에 실패했습니다. 키 이름: {} 오류 메시지: {}", keyName, e.getMessage(), e);
			throw new FileDeleteException();
		}
	}

	/**
	 * update 버킷 파일
	 * @param file 새로운 파일
	 * @param keyName 이미 존재하는 key
	 * @return keyName
	 */
	public String updateObject(MultipartFile file, String keyName) {

		try (InputStream inputStream = file.getInputStream()) {

			RequestBody requestBody = RequestBody.fromInputStream(inputStream, file.getSize());
			deleteObject(keyName);

			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucketName)
					.key(keyName)
					.contentType(file.getContentType())
					.contentLength(file.getSize())
					.build(),
				requestBody);

		} catch (IOException e) {
			log.error("S3 파일 업데이트에 실패했습니다. 파일 입력 오류. fileRequest 정보: keyName={}. 오류 메시지: {}",
				keyName, e.getMessage(), e);
			throw new FileUploadException();
		} catch (SdkException e) {
			log.error("S3 파일 업데이트에 실패했습니다. AWS SDK 오류.  fileRequest 정보: keyName={}. 오류 메시지: {}",
				keyName, e.getMessage(), e);
			throw new FileUploadException();
		} catch (Exception e) {
			log.error("S3 파일 업데이트에 실패했습니다. fileRequest 정보: keyName={}. 오류 메시지: {}",
				keyName, e.getMessage(), e);
			throw new FileUploadException();
		}

		return keyName;
	}

}
