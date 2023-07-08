package com.notice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoticeNotFoundException extends RuntimeException {

	public NoticeNotFoundException(String message) {
		super(message);
	}

}
