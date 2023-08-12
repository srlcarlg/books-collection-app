package com.project.bkcollection.api.controllers;

import java.io.IOException;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bkcollection.api.controllers.doc.StorageDOC.DOC_DeleteFile;
import com.project.bkcollection.api.controllers.doc.StorageDOC.DOC_InsertFile;
import com.project.bkcollection.api.controllers.doc.StorageDOC.DOC_SearchFile;
import com.project.bkcollection.api.controllers.doc.StorageDOC.DOC_StorageController;
import com.project.bkcollection.api.handlers.excepts.CustomNotFoundException;
import com.project.bkcollection.core.BooksCollectionPermissions.isLibrarianOrAdmin;
import com.project.bkcollection.core.entities.FileInfo;
import com.project.bkcollection.core.repositories.FileInfoRepository;
import com.project.bkcollection.core.services.storage.adapters.StorageService;

@RestController
@RequestMapping("/files")
@DOC_StorageController
public class StorageController {

    @Autowired
    private FileInfoRepository repository;

    @Autowired
    private StorageService storageService;
    
    @DOC_SearchFile
    @GetMapping("/{filename}")
    public ResponseEntity<Object> searchFile(@PathVariable String filename) throws IOException {
        FileInfo fileInfo = repository.findByFilename(filename)
        		.orElseThrow(() -> new CustomNotFoundException("File '" + filename + "' not found!"));
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
            .header("Content-Type", fileInfo.getContentType())
            .header("Content-Length", fileInfo.getContentLength().toString())
            .body(file.getInputStream().readAllBytes());
    }
    
    @DOC_InsertFile
    @isLibrarianOrAdmin
    @PostMapping(value = "/upload/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> insertFile(@PathVariable String bookId, @ParameterObject @RequestPart MultipartFile file) {
        FileInfo fileInfo = storageService.save(file, bookId);
        return ResponseEntity.ok().body(fileInfo.getUrl());
    }
    
    @DOC_DeleteFile
    @isLibrarianOrAdmin
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long bookId) {
        storageService.deleteFromBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
