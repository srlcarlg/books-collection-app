package com.project.bkcollection.core.services.storage;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project.bkcollection.api.controllers.StorageController;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.services.storage.exceptions.StorageServiceException;

@Service
public class UrlService {

	private static final String[] imgMimes = {"image/jpeg", "image/jpg", "image/png"};
	
	public FileInfo urlToFileInfo(String strUrl, File pathFile, File newPathFile, FileInfo fileInfo) {
		try {
			URL url = new URI(strUrl).toURL();

			String[] urlInfo = getLengthAndType(url);
			Long contentLength = Long.parseLong(urlInfo[0]);
			String contentType = urlInfo[1];
			
			String extension = StringUtils.getFilenameExtension(pathFile.getName());
			
	    	if (!pathFile.exists()) {
	    		Files.createDirectories(pathFile.toPath().getParent());
	    		Files.createFile(pathFile.toPath());
	    	};
			
			if(extension.equals("pdf")) {
				validateContentTypePDF(contentType);
			} else {
				validateContentTypeIMG(contentType);
				String extUrl = contentType.split("/")[1];
				String newName = newPathFile.getName().replace(extension, "") + extUrl;
				
				pathFile = Files.move(pathFile.toPath(),
							newPathFile.toPath().resolveSibling(newName)).toFile();
				
			}
			
			saveFile(url, pathFile);

	        fileInfo.setFilename(pathFile.getName());
	        fileInfo.setContentLength(contentLength);
	        fileInfo.setContentType(contentType);
	        fileInfo.setUrl(
	        	linkTo(methodOn(StorageController.class).searchFile(pathFile.getName())).toString()
	        );
	        
	        return fileInfo;
					
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		} catch (URISyntaxException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		}
		

	}
	private static void saveFile(URL url, File pathFile) throws IOException, FileNotFoundException { 
		ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
		
		FileOutputStream fileOutputStream = new FileOutputStream(pathFile);
		FileChannel fileChannel = fileOutputStream.getChannel();
		
		fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		fileOutputStream.close();	
		fileChannel.close();
	}
	
	private static String[] getLengthAndType(URL url) {
		HttpURLConnection conn = null;
	    try {
	    	conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("HEAD");
	        String[] toSend = {Long.toString(conn.getContentLengthLong()), conn.getContentType()};
	        return toSend;
	    } catch (IOException e) {
	        throw new StorageServiceException(e.getLocalizedMessage());
	    } finally {
	        if(conn != null) {
	            conn.disconnect();
	        }
	    }
	}
	
	private static void validateContentTypePDF(String contentType) {
		if (!contentType.equals("application/pdf")) {
			throw new StorageServiceException("bookUrl: Only MIME type 'application/pdf' is supported!");
		}
	}
	private static void validateContentTypeIMG(String contentType) {
		if (!Arrays.asList(imgMimes).contains(contentType)) {
			throw new StorageServiceException("coverUrl: Only MIME types 'image/(jpeg, jpg, png)' is supported!");
		}
	}
}
