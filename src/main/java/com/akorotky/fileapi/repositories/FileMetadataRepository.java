package com.akorotky.fileapi.repositories;

import com.akorotky.fileapi.domain.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {

    Optional<FileMetadata> findByFileId(String fileId);

    Page<FileMetadata> findByFileIdIn(List<String> fileIds, Pageable pageable);

    void deleteByFileId(String fileId);
}
