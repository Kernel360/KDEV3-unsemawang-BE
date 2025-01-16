package com.palbang.unsemawang.common.util.file.exception;

public class FileDeleteException extends RuntimeException {

	public FileDeleteException() {
	}

	public FileDeleteException(String message) {
		super(message);
	}

	public FileDeleteException(String message, Throwable cause) {
		super(message, cause);
	}
}
