package com.project.bkcollection.core.services.storage.adapters;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.project.bkcollection.api.dtos.internal.BookDTO;
import com.project.bkcollection.api.dtos.request.BookRequest;
import com.project.bkcollection.api.dtos.request.BookUpdateRequest;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.services.storage.exceptions.StorageServiceException;

public interface StorageService {

    FileInfo save(MultipartFile file, String strbookId) throws StorageServiceException;
    void saveFromUrl(BookRequest bookRequest, Long bookId) throws StorageServiceException;

    Resource load(String filename) throws StorageServiceException;
    
    void update(BookUpdateRequest bookRequest, BookDTO bookDTO) throws StorageServiceException;
    
    void delete(BookDTO bookDTO) throws StorageServiceException;
    void deleteFromBook(Long bookId) throws StorageServiceException;

}
