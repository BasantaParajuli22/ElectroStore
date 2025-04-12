package com.example.springTrain.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.InvalidInputException;
import com.example.springTrain.exceptions.ResourceNotFoundException;

@Service
public class FileStorageService {
	
	Logger logger = LoggerFactory.getLogger(FileStorageService.class);
	public String uploadString = System.getProperty("user.dir") +"/src/main/resources/static/uploads/";

	//save File with unique name in uploads dir
    public String saveFile(MultipartFile file) { 	
    	if(file.isEmpty() || file == null) {
    		throw new InvalidInputException(" file is null to save");
    	}
    	//Converts a path string, 
    	//or a sequence of strings that when joined form a path string, to a Path.
    	Path uploadPath = Paths.get(uploadString);
    	
    	if( !Files.exists(uploadPath) ) {//if uploadPath not exists 
			try {
				//create directories if parent directory doesnot exists creates it
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				throw new CreationFailedException("Directory couldnot be created " +uploadPath);
			}
		}
    	
    	try {
			String orginalName = file.getOriginalFilename();
			long millis = System.currentTimeMillis();	//to make unique		
			String uniqueName = millis + "_" + orginalName;
			
			//	c:/src/main/resources/static/uploads/uploadPath -> adds uniqueName
			// 	resolves to c:/src/main/resources/static/uploads/uploadPath/uniqueName
			Path uniqueFilePath = uploadPath.resolve(uniqueName).normalize();
			
			byte[] fileBytes = file.getBytes();//byte array to write 
			Files.write(uniqueFilePath, fileBytes);//needs path and byte array to write
			
			logger.info("file created successfully "+ uniqueFilePath);
			return uniqueName;//returning name to save in db
			
		} catch (IOException e) {
			throw new ResourceNotFoundException("path string cannot be converted to a Path or cannot access to read file " +e);
		}   	
    }
    
    //gets file images or resource 
    public Resource getFileResource(String uniqueFileName) {
    	
    	Path filePath = Paths.get(uploadString).resolve(uniqueFileName).normalize();
    	try {
    		// filePath.toUri(): Converts the Path object to a URI (e.g file:///uploads/file.txt).
    		//Creates a UrlResource object, which is a Spring abstraction for accessing files via their URI.
			Resource resource = new UrlResource(filePath.toUri() );
			
			if(resource.exists()) {				
				return resource;
			}
			logger.warn("couldnot found resource "+ filePath);
			return null;
		} catch (MalformedURLException e) {
			throw new ResourceNotFoundException("couldnot find or invalid filepath to resource" +e);
		}
    }

    
    //if file is pdf or not 
    public boolean ifValidFileType(MultipartFile file) {
    	if(file == null || file.isEmpty()) {
    		return false;
    	}
    	
    	String type = file.getContentType();
    	boolean validType = type.equalsIgnoreCase("application/pdf");
    	
    	if(type == null || !validType ) {
    		return false; 
    	}
    	return true;
    }
    
    //if valid image type 
    public boolean ifValidImageType(MultipartFile file) {
    	if(file == null || file.isEmpty()) {
    		return false;
    	}
    	Set<String> allowedTypes = Set.of("image/jpg","image/jpeg","image/webp","image/png");
    	String contentType = file.getContentType();
    	
    	//immutable form of Set for faster lookups
    	if(contentType != null && allowedTypes.contains(contentType.toLowerCase() )) {
    		return true;
    	}
    	return false;
    }
    
    public String getContentType(MultipartFile file) {
    	if(file == null || file.isEmpty()) {
    		return null;
    	}
    	Set<String> allowedTypes = Set.of("application/pdf","image/jpg","image/jpeg","image/webp","image/png");
    	
    	String contentType = file.getContentType();
    	if(contentType != null &&  allowedTypes.contains(contentType.toLowerCase()) ) {	
    		return contentType;
    	}
    	logger.warn("file not a valid image type "+contentType);
    	return null;
    }
    
    public String getContentTypeOfResource(Resource resource) {
    	if(resource == null || !resource.exists()) {
			throw new InvalidInputException("resource is null or doesnot exists "+resource);
    	}

    	try {
			Path path = resource.getFile().toPath();
			String contentType = Files.probeContentType(path);
			return contentType;
		} catch (IOException e) {
			logger.warn("resorce not found "+resource);
			return "application/octet-stream"; //default if contentType not found
		}
    }
    
}
