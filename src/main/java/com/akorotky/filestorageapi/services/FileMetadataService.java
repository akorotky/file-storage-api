package com.akorotky.filestorageapi.services;

import com.akorotky.filestorageapi.constants.DocumentMimeType;
import com.akorotky.filestorageapi.constants.EnumUtils;
import com.akorotky.filestorageapi.domain.FileMetadata;
import com.akorotky.filestorageapi.dtos.FileMetadataMapper;
import com.akorotky.filestorageapi.dtos.FileMetadataRequestDto;
import com.akorotky.filestorageapi.repositories.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileMetadataService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataMapper fileMetadataMapper;

    public Optional<FileMetadata> findMetadataByFileId(String fileId) {
        return fileMetadataRepository.findByFileId(fileId);
    }

    public Page<FileMetadata> findAllFileMetadata(Pageable pageable) {
        return fileMetadataRepository.findAll(pageable);
    }

    public Page<FileMetadata> findFileMetadataByFileIds(List<String> fileIds, Pageable pageable) {
        return fileMetadataRepository.findByFileIdIn(fileIds, pageable);
    }

    public void populateFileMetadata(MultipartFile file, FileMetadataRequestDto fileMetadataRequestDto, String fileId) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileId(fileId);
        fileMetadata.setFilename(file.getOriginalFilename());
        fileMetadata.setFileSize(file.getSize());
        DocumentMimeType fileType = EnumUtils.fromValue(DocumentMimeType.class, file.getContentType());
        fileMetadata.setFileType(fileType);
        fileMetadata.setUser(fileMetadataMapper.toUser(fileMetadataRequestDto));
        fileMetadataRepository.save(fileMetadata);
    }

    public void deleteFileMetadataByFileId(String fileId) {
        fileMetadataRepository.deleteByFileId(fileId);
    }
}
