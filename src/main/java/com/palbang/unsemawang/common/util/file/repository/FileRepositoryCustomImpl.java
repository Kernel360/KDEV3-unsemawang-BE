package com.palbang.unsemawang.common.util.file.repository;

import static com.palbang.unsemawang.common.util.file.entity.QFile.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.palbang.unsemawang.common.util.file.dto.FileRequest;
import com.palbang.unsemawang.common.util.file.entity.File;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileRepositoryCustomImpl implements FileRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<File> findFilesByFileReference(FileRequest fileRequest) {

		if (fileRequest.referenceStringId() == null) {
			return queryFactory
				.select(file)
				.from(file)
				.where(
					file.referenceId.eq(fileRequest.referenceId())
						.and(file.referenceType.eq(fileRequest.referenceType()))
				)
				.orderBy(file.id.asc())
				.fetch();
		} else {
			return queryFactory
				.select(file)
				.from(file)
				.where(
					file.referenceStringId.eq(fileRequest.referenceStringId())
						.and(file.referenceType.eq(fileRequest.referenceType()))
				)
				.orderBy(file.id.asc())
				.fetch();
		}
	}

	@Override
	public List<File> findFilesByFileReferenceAndIsNotDeleted(FileRequest fileRequest) {
		if (fileRequest.referenceStringId() == null) {
			return queryFactory
				.select(file)
				.from(file)
				.where(
					file.referenceId.eq(fileRequest.referenceId())
						.and(file.referenceType.eq(fileRequest.referenceType()))
						.and(file.isDeleted.isFalse())
				)
				.orderBy(file.id.asc())
				.fetch();
		} else {
			return queryFactory
				.select(file)
				.from(file)
				.where(
					file.referenceStringId.eq(fileRequest.referenceStringId())
						.and(file.referenceType.eq(fileRequest.referenceType()))
						.and(file.isDeleted.isFalse())
				)
				.orderBy(file.id.asc())
				.fetch();
		}
	}
}
