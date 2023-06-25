package com.akorotky.fileapi.services;

import com.akorotky.fileapi.constants.DocumentMimeType;
import com.akorotky.fileapi.constants.EnumUtils;
import com.akorotky.fileapi.domain.FileMetadata;
import com.akorotky.fileapi.dtos.FileMetadataMapper;
import com.akorotky.fileapi.dtos.FileMetadataRequestDto;
import com.akorotky.fileapi.exceptions.ResourceNotFoundException;
import com.akorotky.fileapi.repositories.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileMetadataService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataMapper fileMetadataMapper;
    private final String FILE_METADATA_NOT_FOUND_MSG = "No file metadata found";

    public FileMetadata findMetadataByFileId(String fileId) {
        return fileMetadataRepository.findByFileId(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File metadata for file with id=" + fileId + " not found"));
    }

    public Page<FileMetadata> findAllOrByFileIds(List<String> fileIds, Pageable pageable) {
        if (fileIds != null) return findFileMetadataByFileIds(fileIds, pageable);
        else return findAllFileMetadata(pageable);

    }

    public Page<FileMetadata> findAllFileMetadata(Pageable pageable) {
        Page<FileMetadata> fileMetadata = fileMetadataRepository.findAll(pageable);
        if (fileMetadata.isEmpty()) throw new ResourceNotFoundException(FILE_METADATA_NOT_FOUND_MSG);
        return fileMetadata;
    }

    public Page<FileMetadata> findFileMetadataByFileIds(List<String> fileIds, Pageable pageable) {
        Page<FileMetadata> fileMetadata = fileMetadataRepository.findByFileIdIn(fileIds, pageable);
        if (fileMetadata.isEmpty()) throw new ResourceNotFoundException(FILE_METADATA_NOT_FOUND_MSG);
        return fileMetadata;
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
