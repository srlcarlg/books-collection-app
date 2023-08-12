package com.project.bkcollection.core.services.storage;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project.bkcollection.api.controllers.StorageController;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.services.storage.exceptions.StorageServiceException;

@Service
public class PdfImageService {
	
	public FileInfo firstPageToImage(Path filePath, Path authorPath, FileInfo fileInfo ) {
		File sourceFile = filePath.toFile();
		String extension = StringUtils.getFilenameExtension(sourceFile.getName());
		
		PDDocument pdfDocument;
		PDFRenderer pdfRenderer;
        try {
    		String strImgPath = String.format(
    			getImagesPath(authorPath) + "/" + sourceFile.getName().replace("."+extension, ".png")
    		);
			pdfDocument = PDDocument.load(sourceFile);
	        pdfRenderer = new PDFRenderer(pdfDocument);
	        
            BufferedImage bffImage = pdfRenderer.renderImageWithDPI(0, 72, ImageType.RGB);
            ImageIOUtil.writeImage(bffImage, strImgPath, 72);
            pdfDocument.close();
            return getFileInfoImage(strImgPath, fileInfo);
            
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		}
	}
	
	private Path getImagesPath(Path authorPath) throws IOException {
		Path imgPath = Paths.get(
	        String.format(authorPath + "/images")
	    );
    	if (!Files.exists(imgPath)) {
    		Files.createDirectories(imgPath);
    	};
		return imgPath;
	}
	
	private FileInfo getFileInfoImage(String strArchivePath, FileInfo fileInfo) throws IOException {
		File imgArchive = Paths.get(strArchivePath).toFile();
	    URLConnection conn = imgArchive.toURI().toURL().openConnection();
	    
        fileInfo.setFilename(imgArchive.getName());
        fileInfo.setContentLength(imgArchive.length());
        fileInfo.setContentType(conn.getContentType());
        fileInfo.setUrl(
        	linkTo(methodOn(StorageController.class).searchFile(imgArchive.getName())).toString()
        );
        return fileInfo;
	}
}
