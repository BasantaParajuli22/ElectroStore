package com.example.springTrain.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.service.FileStorageService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	private final FileStorageService fileStorageService;
	
	public FileController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}
	
	@GetMapping("/view")
	public ResponseEntity<Resource> viewResource(String fileName){
		
		Resource resource = fileStorageService.getFileResource(fileName);
		
		String contentType = fileStorageService.getContentType(null);
		String contentDisposition = "inline;filename=\"" +fileName+ "\" ";
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(resource);
		
	}
	
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadResource(String fileName){
		
		Resource resource = fileStorageService.getFileResource(fileName);

		String contentType = fileStorageService.getContentType(null);
		String contentDisposition = "attachment;filename=\"" +fileName+ "\" ";
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(resource);
	}


}
