package com.notice.controller.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResult {

	private int code;
	
	private String message;
	
}
