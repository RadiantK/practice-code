package com.notice.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.notice.domain.AttachFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtils {
	
	@Value("${file.dir}")
	private String fileDir;
	
	/**
	 * 여러 파일 저장
	 */
	public List<AttachFile> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
		
		List<AttachFile> attachFiles = new ArrayList<>();
		
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				attachFiles.add(saveFile(multipartFile));
			}
		}
		
		return attachFiles;
	}
	
	/**
	 * 단일 파일 저장
	 */
	public AttachFile saveFile(MultipartFile multipartFile) throws IOException {
		if (multipartFile.isEmpty()) {
			return null;
		}

		if (!checkExtention(multipartFile.getOriginalFilename())) {
			return null;
		}
		
		String orgFileName = multipartFile.getOriginalFilename();
		String saveFileName = createSaveFileName(multipartFile.getOriginalFilename());
		long fileSize = multipartFile.getSize();
		
		log.info("orgFileName={}", orgFileName);
		log.info("saveFileName={}", saveFileName);
		log.info("fileSize={}", fileSize);
		log.info("getFullPath={}", getFullPathFileName(saveFileName));
		
		multipartFile.transferTo(new File(getFullPathFileName(saveFileName)));
		
		return new AttachFile(
				orgFileName, 
				getCurrentDate() + File.separator +  saveFileName, 
				extractExtention(extractExtention(multipartFile.getOriginalFilename())), 
				fileSize);
	}
	
	// 파일 확장자 체크
	private boolean checkExtention(String fileName) {
		// 확장자 찾기
		String temp = extractExtention(fileName);
		String[] ext = {"jpg", "jpeg", "png"};
		
		for (int i = 0; i < ext.length; i++) {
			if (ext[i].equals(temp)) return true;
		}
		
		return false;
	}

	// 파일의 전체 경로 얻기
	public String getFullPathFileName(String fileName) {
		
		String currentDate = getCurrentDate();
		
		// 디렉토리 생성
		File file = new File(this.fileDir + currentDate);
		if (!file.exists()) {
			file.mkdir();
		}
		
		return file.getAbsolutePath() + File.separator + fileName;
	}

	// 파일 저장 현재날짜 포맷 형식
	public String getCurrentDate() {
		LocalDateTime ldt = LocalDateTime.now();
		String format = ldt.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
		return format;
	}
	
	// 파일의 전체 경로 얻기
	public String getFullPathSaveFileName(String fileName) {
		
		return fileDir + fileName;
	}
	
	// UUID 파일명 생성
	private String createSaveFileName(String orgFileName) {
		String uuid = UUID.randomUUID().toString();
		String extention = extractExtention(orgFileName);
		
		return uuid + "." + extention;
	}
	
	// 파일 확장자 추출
	private String extractExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos + 1);
	}
	
	public String getUploadFileDirectory() {
		return fileDir;
	}

}
