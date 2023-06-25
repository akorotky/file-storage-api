package com.akorotky.fileapi.services;

import com.akorotky.fileapi.constants.Constants;
import com.akorotky.fileapi.constants.DocumentMimeType;
import com.akorotky.fileapi.constants.EnumUtils;
import com.akorotky.fileapi.domain.File;
import com.akorotky.fileapi.domain.FileMetadata;
import com.akorotky.fileapi.dtos.FileMetadataMapper;
import com.akorotky.fileapi.dtos.FileMetadataRequestDto;
import com.akorotky.fileapi.repositories.FileMetadataRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final GridFsOperations gridFsOperations;
    private final FileMetadataMapper fileMetadataMapper;
    private final FileMetadataRepository fileMetadataRepository;

    public String uploadDocument(MultipartFile file, FileMetadataRequestDto fileMetadataRequestDto) throws IOException, HttpMediaTypeNotSupportedException {
        String filename = file.getOriginalFilename();
        String fileType = file.getContentType();
        if (!Constants.allowedDocumentMimeTypes.contains(fileType))
            throw new HttpMediaTypeNotSupportedException("Unsupported file type: " + fileType);
        if(filename == null){
            throw new RuntimeException("Filename must not be null");
        }
        FileMetadata fileMetadata = populateFileMetadata(file, fileMetadataRequestDto);
        fileMetadataRepository.save(fileMetadata);
        ObjectId fileId = gridFsOperations.store(file.getInputStream(), filename);
        return fileId.toString();
    }

    public File downloadDocument(String filename) throws IOException {
        GridFSFile gridFSFile = gridFsOperations.findOne(new Query(Criteria.where("filename").is(filename)));
        if (gridFSFile == null) throw new RuntimeException("File not found");
        File file = new File();
        file.setFilename(filename);
        file.setStream(gridFsOperations.getResource(gridFSFile).getInputStream());
        return file;
    }

    public FileMetadata populateFileMetadata(MultipartFile file, FileMetadataRequestDto fileMetadataRequestDto) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFilename(file.getOriginalFilename());
        fileMetadata.setFileSize(file.getSize());
        DocumentMimeType fileType = EnumUtils.fromValue(DocumentMimeType.class, file.getContentType());
        fileMetadata.setFileType(fileType);
        fileMetadata.setUser(fileMetadataMapper.toUser(fileMetadataRequestDto));
        return fileMetadata;
    }

    public FileMetadata getFileMetadata() {
        return new FileMetadata();
    }
}
