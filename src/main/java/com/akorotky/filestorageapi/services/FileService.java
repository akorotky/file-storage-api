package com.akorotky.filestorageapi.services;

import com.akorotky.filestorageapi.constants.Constants;
import com.akorotky.filestorageapi.domain.File;
import com.akorotky.filestorageapi.dtos.FileMetadataRequestDto;
import com.akorotky.filestorageapi.exceptions.ResourceNotFoundException;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final GridFsOperations gridFsOperations;
    private final FileMetadataService fileMetadataService;
    private final GridFsTemplate gridFsTemplate;

    public String uploadFile(MultipartFile file, FileMetadataRequestDto fileMetadataRequestDto) throws IOException, HttpMediaTypeNotSupportedException {
        String filename = verifyFileValidity(file);
        String fileId = gridFsOperations.store(file.getInputStream(), filename).toString();
        fileMetadataService.populateFileMetadata(file, fileMetadataRequestDto, fileId);
        return fileId;
    }

    public File downloadFile(String documentId) throws IOException {
        GridFSFile gridFSFile = findFileById(documentId);
        File file = new File();
        file.setFilename(gridFSFile.getFilename());
        file.setStream(gridFsOperations.getResource(gridFSFile).getInputStream());
        return file;
    }

    public void deleteFile(String documentId) {
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

    public void downloadFilesAsZip(List<String> fileIds, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        GridFSFindIterable filesIterable = gridFsOperations.find(new Query(Criteria.where("_id").in(fileIds)));
        for (GridFSFile file : filesIterable) {
            GridFsResource resource = gridFsTemplate.getResource(file);
            ZipEntry zipEntry = new ZipEntry(file.getFilename());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.close();
    }
}
