package com.notice.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiSingleDataResponse<T> {
	
	private T data;
}
