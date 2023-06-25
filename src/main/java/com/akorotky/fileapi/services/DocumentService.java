package com.akorotky.fileapi.services;

import com.akorotky.fileapi.constants.Constants;
import com.akorotky.fileapi.domain.File;
import com.akorotky.fileapi.dtos.FileMetadataRequestDto;
import com.akorotky.fileapi.exceptions.ResourceNotFoundException;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {

    private final GridFsOperations gridFsOperations;
    private final FileMetadataService fileMetadataService;

    public String uploadDocument(MultipartFile file, FileMetadataRequestDto fileMetadataRequestDto) throws IOException, HttpMediaTypeNotSupportedException {
        String filename = verifyFileValidity(file);
        String fileId = gridFsOperations.store(file.getInputStream(), filename).toString();
        fileMetadataService.populateFileMetadata(file, fileMetadataRequestDto, fileId);
        return fileId;
    }

    public File downloadDocument(String documentId) throws IOException {
        GridFSFile gridFSFile = findFileById(documentId);
        File file = new File();
        file.setFilename(gridFSFile.getFilename());
        file.setStream(gridFsOperations.getResource(gridFSFile).getInputStream());
        return file;
    }

    public void deleteDocument(String documentId) {
        gridFsOperations.delete(new Query(Criteria.where("_id").is(documentId)));
        fileMetadataService.deleteFileMetadataByFileId(documentId);
    }

    public GridFSFile findFileById(String fileId) {
        GridFSFile gridFSFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (gridFSFile == null) throw new ResourceNotFoundException("File with id=" + fileId + " not found");
        return gridFSFile;
    }

    public String verifyFileValidity(MultipartFile file) throws HttpMediaTypeNotSupportedException {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename must not be empty");
        }
        String fileType = file.getContentType();
        if (!Constants.allowedDocumentMimeTypes.contains(fileType))
            throw new HttpMediaTypeNotSupportedException("Unsupported file type: " + fileType);
        return filename;
    }
}
