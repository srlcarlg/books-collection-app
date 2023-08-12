package com.project.bkcollection.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bkcollection.core.entities.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>{
	
	Optional<FileInfo> findByFilename(String filename);
}
