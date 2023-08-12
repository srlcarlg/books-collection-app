package com.project.bkcollection.core.services.storage.providers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.bkcollection.api.controllers.StorageController;
import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.api.handlers.excepts.CustomFieldErrorException;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.api.handlers.models.FieldErrorMessage;
import com.project.bkcollection.api.mappers.BookMapper;
import com.project.bkcollection.core.entities.Author;
import com.project.bkcollection.core.entities.Book;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.entities.enums.BookStatus;
import com.project.bkcollection.core.repositories.AuthorRepository;
import com.project.bkcollection.core.repositories.BookRepository;
import com.project.bkcollection.core.repositories.FileInfoRepository;
import com.project.bkcollection.core.services.storage.PdfImageService;
import com.project.bkcollection.core.services.storage.UrlService;
import com.project.bkcollection.core.services.storage.adapters.StorageService;
import com.project.bkcollection.core.services.storage.exceptions.StorageServiceException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class LocalStorageService implements StorageService {
    /* Without "@Transactional" will throws 
       "Could not initialize proxy [Entity] - no Session"  
       when running as an Event
	 */
    @Autowired
    private FileInfoRepository repository;
    
    @Autowired
    private BookRepository bookRepository;
	@Autowired
	private BookMapper bookMapper;
    @Autowired
    private AuthorRepository authorRepository;
        
    @Autowired
    private PdfImageService pdfService;
    @Autowired
    private UrlService urlService;

    private static final Path rootPath = Paths.get("files");
	private static final String[] imgExts = {"jpeg", "jpg", "png"};
	
    @Transactional
	@Override
	public FileInfo save(MultipartFile file, String strBookId) throws StorageServiceException {
		validateFileExt(file);
		try {
            return trySave(file, strBookId);
        } catch (IOException e) {
            throw new StorageServiceException(e.getLocalizedMessage());
        }
	}
    
    @Transactional
	@Override
	public void saveFromUrl(BookRequest bookRequest, Long bookId) throws StorageServiceException {
		try {
			trySaveFromUrl(bookRequest, bookId);
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
    }
    
	@Transactional(readOnly = true)
	@Override
    public Resource load(String filename) {
        try {       
            return trySearch(filename);
        } catch (MalformedURLException e) {
            throw new StorageServiceException(e.getLocalizedMessage());
        }
    }
	
	@Transactional
	@Override
	public void update(BookUpdateRequest bookRequest, BookDTO bookDTO) throws StorageServiceException {
		try {
			tryRenameAndMove(bookRequest, bookDTO);
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	
	@Transactional
	@Override
	public void delete(BookDTO bookDTO) throws StorageServiceException {
		try {
			tryDelete(bookDTO);
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
	@Transactional
	@Override
	public void deleteFromBook(Long bookId) throws StorageServiceException {
		try {
			tryDeleteFromBook(bookId);
		} catch (IOException e) {
			throw new StorageServiceException(e.getLocalizedMessage());
		} catch (EntityNotFoundException e) {
			throw new CustomNotFoundException();
		}
	}
    
    private FileInfo trySave(MultipartFile file, String strBookId) throws IOException {

    	Book book = bookRepository.findById(stringToLong(strBookId))
				.orElseThrow(() -> 	new CustomNotFoundException());
    	
		String authorName = getAuthorInfo(book).getName();
    	Path authorPath = getAuthorPath(authorName);
    	Path coverPath = getCoverPath(authorPath);
    	
    	if (!Files.exists(authorPath)) {
    		Files.createDirectories(authorPath);
    		Files.createDirectories(coverPath);
    	};
    	
    	String bookFilename = formatTitleToPDF(book.getTitle());
    	Path archivePath = authorPath.resolve(bookFilename);
    	
    	try {
    		Files.copy(file.getInputStream(), archivePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("bookId", e.getLocalizedMessage());
			throw new CustomFieldErrorException(fieldErrorMessage);
		}
    	
    	File bookArchive = archivePath.toFile();
    	FileInfo bookFileInfo = book.getBookFile() == null ? new FileInfo() : book.getBookFile();
		if (bookArchive.exists()) {
	    	bookFileInfo = saveFileInfo(bookFileInfo, bookFilename, file);
		}
		
		FileInfo coverFileInfo = book.getCoverFile() == null ? new FileInfo() : book.getCoverFile();
		if (coverFileInfo.getFilename() != null) {
			coverPath.resolve(coverFileInfo.getFilename()).toFile()
			.delete();
		}
		coverFileInfo = pdfService.firstPageToImage(archivePath, authorPath, coverFileInfo);
    	
		File coverArchive = coverPath.resolve(coverFileInfo.getFilename()).toFile();
		if (coverArchive.exists()) { 
			coverFileInfo = repository.save(coverFileInfo);
		}
		
		boolean bookCondition = bookArchive.exists();
		boolean coverCondition = coverArchive.exists();
		
		book.setBookFile(bookCondition ? bookFileInfo : null);
        book.setCoverFile(coverCondition ? coverFileInfo : null);
        book.setBookStatus(bookCondition ? BookStatus.AVAILABLE : book.getBookStatus());
        bookRepository.save(book);
        
        return bookFileInfo;
    }
    
    private void trySaveFromUrl(BookRequest bookRequest, Long bookId) throws IOException, EntityNotFoundException {
    	Book book = bookRepository.getReferenceById(bookId);
    	
		String authorName = getAuthorInfo(book).getName();
    	Path authorPath = getAuthorPath(authorName);
    	Path coverPath = getCoverPath(authorPath);
    	
    	if (!Files.exists(authorPath)) {
    		Files.createDirectories(authorPath);
    		Files.createDirectories(coverPath);
    	};
    	
    	FileInfo bookFileInfo = new FileInfo();
    	FileInfo coverFileInfo= new FileInfo();
    	
    	String bookFilename = formatTitleToPDF(book.getTitle());
    	String coverFilename = bookFilename.replace(".pdf", ".png");
    	
    	File bookArchive = authorPath.resolve(bookFilename).toFile();
    	File coverArchive = coverPath.resolve(coverFilename).toFile();

        if (bookRequest.getBookUrl() != null) {
        	bookFileInfo = urlService.urlToFileInfo(bookRequest.getBookUrl(), bookArchive, bookArchive, bookFileInfo);
        	bookArchive = authorPath.resolve(bookFileInfo.getFilename()).toFile();
			if (bookArchive.exists()) {
				bookFileInfo = repository.save(bookFileInfo);
			}
		}
		if (bookRequest.getCoverUrl() != null) {
        	coverFileInfo = urlService.urlToFileInfo(bookRequest.getCoverUrl(), coverArchive, coverArchive, coverFileInfo);
        	coverArchive = coverPath.resolve(coverFileInfo.getFilename()).toFile();
			if (coverArchive.exists()) {
				coverFileInfo = repository.save(coverFileInfo);
			}
		}
		
		boolean bookCondition = bookArchive.exists();
		boolean coverCondition = coverArchive.exists();
		
		book.setBookFile(bookCondition ? bookFileInfo : null);
        book.setCoverFile(coverCondition ? coverFileInfo : null);
        book.setBookStatus(bookCondition ? BookStatus.AVAILABLE : book.getBookStatus());
        bookRepository.save(book);
    }
    
    private UrlResource trySearch(String filename) throws MalformedURLException  {
    	
    	String authorName = getAuthorNameFromFileName(filename);
    	Path authorPath = getAuthorPath(authorName);
    	
    	Path archivePath;
		String extension = StringUtils.getFilenameExtension(filename);   
		
        if (Arrays.asList(imgExts).contains(extension)) {
        	Path coverPath = getCoverPath(authorPath);
        	archivePath = coverPath.resolve(filename);
        }
        else {
        	archivePath = authorPath.resolve(filename);
        }
        
        return new UrlResource(archivePath.toUri());
    }
    
    private void tryRenameAndMove(BookUpdateRequest bookRequest, BookDTO bookDTO) throws IOException {

		Author authorDB = getAuthorInfo(bookDTO);
		Author authorRequest = bookRequest.getAuthors().isEmpty() ? authorDB : getAuthorInfo(bookRequest);
				
    	Path authorPath = getAuthorPath(authorDB.getName());
    	Path coverPath = getCoverPath(authorPath);

		FileInfo bookFileInfo = bookDTO.getBookFile() == null ? new FileInfo() : bookDTO.getBookFile();
		FileInfo coverFileInfo = bookDTO.getCoverFile() == null ? new FileInfo() : bookDTO.getCoverFile();
    	
    	String bookFilename = bookFileInfo.getFilename() == null ? formatTitleToPDF(bookDTO.getTitle()) : bookFileInfo.getFilename();
    	String coverFilename = coverFileInfo.getFilename() == null ? bookFilename.replace(".pdf", ".png") : coverFileInfo.getFilename();

    	File bookArchive = authorPath.resolve(bookFilename).toFile();
    	File coverArchive = coverPath.resolve(coverFilename).toFile();
    	
		if (!authorRequest.getId().equals(authorDB.getId())) {
			authorPath = getAuthorPath(authorRequest.getName());
			coverPath = getCoverPath(authorPath);
	    	if (!Files.exists(authorPath)) {
	    		Files.createDirectories(authorPath);
	    		Files.createDirectories(coverPath);
	    	};
		}

		if (bookRequest.getTitle() != null) { 
			if (!bookRequest.getTitle().equals(bookDTO.getTitle())) {
				bookFilename = formatTitleToPDF(bookRequest.getTitle());
				
				String extension = StringUtils.getFilenameExtension(coverFilename);   
				coverFilename = bookFilename.replace(".pdf", "."+extension);
				coverPath = getCoverPath(authorPath);
			}
		}
		
		String originFilename = formatTitleToPDF(bookDTO.getTitle());
		if (!authorRequest.getId().equals(authorDB.getId()) || !bookFilename.equals(originFilename)
			|| bookRequest.getBookUrl() != null || bookRequest.getCoverUrl() != null) {

    		if (bookRequest.getBookUrl() != null) {
    			File newNameArchive = coverPath.resolve(coverFilename).toFile();
    			bookFileInfo = urlService.urlToFileInfo(bookRequest.getBookUrl(), bookArchive, newNameArchive, bookFileInfo);
    			
    			bookArchive = authorPath.resolve(bookFileInfo.getFilename()).toFile();
    			if (bookArchive.exists()) {
    				bookFileInfo = repository.save(bookFileInfo);
    			}
    		} else {
        		bookArchive.renameTo(authorPath.resolve(bookFilename).toFile());
    			if (bookArchive.exists()) {
    				bookFileInfo = saveFileInfo(bookFileInfo, bookFilename);	
    			}
    		}
	    	
    		if (bookRequest.getCoverUrl() != null) {
    			File newNameArchive = coverPath.resolve(coverFilename).toFile();
    			coverFileInfo = urlService.urlToFileInfo(bookRequest.getCoverUrl(), coverArchive, newNameArchive, coverFileInfo);

    			coverArchive = coverPath.resolve(coverFileInfo.getFilename()).toFile();
    			if (coverArchive.exists()) {
    				coverFileInfo = repository.save(coverFileInfo);
    			}
    		} else {
        		coverArchive.renameTo(coverPath.resolve(coverFilename).toFile());
        		if (coverArchive.exists()) {
        			coverFileInfo = saveFileInfo(coverFileInfo, coverFilename);	
        		}
    		}
    		
    		boolean bookCondition = bookArchive.exists();
    		boolean coverCondition = coverArchive.exists();
    		
    		Book book = bookRepository.getReferenceById(bookDTO.getId());
    		book.setBookFile(bookCondition ? bookFileInfo : null);
	        book.setCoverFile(coverCondition ? coverFileInfo : null);
	        book.setBookStatus(bookCondition ? BookStatus.AVAILABLE : book.getBookStatus());
	        bookRepository.save(book);
	        
		}
    }
    
    private void tryDelete(BookDTO bookDTO) throws IOException {
		
    	String authorName = getAuthorInfo(bookDTO).getName();
    	Path authorPath = getAuthorPath(authorName);
    	
    	String bookFilename = bookDTO.getBookFile() == null ? null : bookDTO.getBookFile().getFilename() ;
    	String coverFilename = bookDTO.getCoverFile() == null ? null : bookDTO.getCoverFile().getFilename() ;
    	
    	if (bookFilename != null) {
	    	File bookArchive = authorPath.resolve(bookFilename).toFile();
	    	if (bookArchive.exists()) {
	    		bookArchive.delete();
	        	FileInfo fileInfo = bookDTO.getBookFile();
	        	repository.delete(fileInfo);
	    	}
    	}
	    if (coverFilename != null) {
	    	File coverArchive = getCoverPath(authorPath).resolve(coverFilename).toFile();
	    	if (coverArchive.exists()) {
	    		coverArchive.delete();
	        	FileInfo coverInfo = bookDTO.getCoverFile();
	        	repository.delete(coverInfo);
	    	}
	    }
    }
    private void tryDeleteFromBook(Long bookId) throws IOException {
    	
    	Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> 	new CustomNotFoundException());
    	
    	tryDelete(bookMapper.toDTO(book));
    	
    	book.setBookFile(null);
    	book.setCoverFile(null);
    	book.setBookStatus(BookStatus.MISSING);
	    bookRepository.save(book);
    }
    
    private Author getAuthorInfo(Book book) {
    	return book.getAuthors().stream().findFirst().get();
    }    
    private Author getAuthorInfo(BookUpdateRequest bookRequest) {
    	Long firstId = bookRequest.getAuthors().stream().findFirst().get().getId();
    	return authorRepository.getReferenceById(firstId);
    }   
    private Author getAuthorInfo(BookDTO book) {
    	return book.getAuthors().stream().findFirst().get();
    }
    
    private Path getAuthorPath(String authorName) {
    	return Paths.get(
        	String.format(rootPath + "/%s", authorName)
        );
    }
    private Path getCoverPath(Path authorPath) {
    	return Paths.get(
        	String.format(authorPath + "/images")
        );
    }
    
    private String getAuthorNameFromFileName(String filename) {
		String extension = StringUtils.getFilenameExtension(filename);
		String bookTitle = formatHyphenToSpace(filename.replace("." + extension, ""));
		
    	Book book = bookRepository.findByTitle(bookTitle)
    			.orElseThrow(() -> new CustomNotFoundException("Title not found!"));

    	return book.getAuthors().stream().findFirst().map(x -> x.getName()).get();
    }
    
    private static String formatTitleToPDF(String title) {
    	return formatSpaceToHyphen(title) + ".pdf";
    } 
    private static String formatSpaceToHyphen(String str) {
    	return str.replaceAll("\\s+","-");
    }
    private static String formatHyphenToSpace(String str) {
    	return str.replaceAll("-"," ");
    }
    
    private static Long stringToLong(String str) {
    	try {
    		return Long.parseLong(str);
    	}
    	catch (NumberFormatException exception) {
    		FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("bookId", "Should be a number!");
			throw new CustomFieldErrorException(fieldErrorMessage);
		}
    }
    
    private void validateFileExt(MultipartFile file) {
		String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
		
		if (!extension.toString().equals("pdf")) {
    		FieldErrorMessage fieldErrorMessage = new FieldErrorMessage("file", "Only .pdf is supported!");
			throw new CustomFieldErrorException(fieldErrorMessage);
		}
	}
       
	private FileInfo saveFileInfo(FileInfo fileInfo, String fileName) throws IOException {
        fileInfo.setFilename(fileName);
        fileInfo.setContentLength(fileInfo.getContentLength());
        fileInfo.setContentType(fileInfo.getContentType());
        fileInfo.setUrl(
        	linkTo(methodOn(StorageController.class).searchFile(fileName)).toString()
        );
        
        return repository.save(fileInfo);
	}
	private FileInfo saveFileInfo(FileInfo fileInfo, String fileName, MultipartFile archive) throws IOException {
        fileInfo.setFilename(fileName);
        fileInfo.setContentLength(archive.getSize());
        fileInfo.setContentType(archive.getContentType());
        fileInfo.setUrl(
        	linkTo(methodOn(StorageController.class).searchFile(fileName)).toString()
        );
        return repository.save(fileInfo);
	}
}
