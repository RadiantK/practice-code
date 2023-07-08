package com.notice.controller.api;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.notice.domain.AttachFile;
import com.notice.repository.AttachFileRepository;
import com.notice.util.FileUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiAttachFileController {

	private final FileUtils fileUtils;
	private final AttachFileRepository attachFileRepository;

	@GetMapping("/attach/images/{id}")
	public ResponseEntity<Resource> showImage(@PathVariable Long id) throws MalformedURLException {
		AttachFile findAttachFile = attachFileRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("파일을 찾을 수 없습니다."));

		String filePath = fileUtils.getUploadFileDirectory() + findAttachFile.getSaveFileName();

		UrlResource urlResource = new UrlResource("file:" + filePath);

		if (!urlResource.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(
				urlResource,
				HttpStatus.OK);
	}

	@GetMapping("/attach/download/{id}")
	public ResponseEntity<Resource> downloadAttachFile(
			@PathVariable Long id) throws MalformedURLException
	{
		AttachFile findAttachFile = attachFileRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("파일을 찾을 수 없습니다."));

		String saveFileName = findAttachFile.getSaveFileName();
		String orgFileName = findAttachFile.getOrgFileName();

		String filePath = fileUtils.getUploadFileDirectory() + saveFileName;

		UrlResource resource = new UrlResource("file:" + filePath);

		String encodedUploadFileName = UriUtils.encode(orgFileName, StandardCharsets.UTF_8);
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}
}
