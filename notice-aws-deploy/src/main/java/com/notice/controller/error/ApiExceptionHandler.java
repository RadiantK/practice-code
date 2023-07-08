package com.notice.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.notice.exception.MemberNotFoundException;
import com.notice.exception.NoticeNotFoundException;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult illgalStateException(NoticeNotFoundException e) {
		return ErrorResult.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult illgalStateException(MemberNotFoundException e) {
		return ErrorResult.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult illgalStateException(IllegalStateException e) {
		return ErrorResult.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult illgalStateException(IllegalArgumentException e) {
		return ErrorResult.builder()
				.code(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResult exception(RuntimeException e) {
		return ErrorResult.builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResult exception(Exception e) {
		return ErrorResult.builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build();
	}
}
