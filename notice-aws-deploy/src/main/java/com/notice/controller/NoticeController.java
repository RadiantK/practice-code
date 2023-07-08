package com.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@GetMapping("/list")
	public String noticeList() {
		return "notice/list";
	}
	
	@GetMapping("/regist")
	public String registPage() {
		return "notice/regist";
	}
	
	@GetMapping("/{id}/detail")
	public String detailPage(Long id) {
		return "notice/detail";
	}
	
	@GetMapping("{id}/edit")
	public String editPage(Long id) {
		return "notice/edit";
	}
}
