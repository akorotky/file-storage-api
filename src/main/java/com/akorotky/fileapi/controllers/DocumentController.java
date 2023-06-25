package com.akorotky.fileapi.controllers;

import com.akorotky.fileapi.domain.File;
import com.akorotky.fileapi.domain.FileMetadata;
import com.akorotky.fileapi.dtos.FileMetadataMapper;
import com.akorotky.fileapi.dtos.FileMetadataRequestDto;
import com.akorotky.fileapi.dtos.FileMetadataResponseDto;
import com.akorotky.fileapi.services.DocumentService;
import com.akorotky.fileapi.services.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final FileMetadataService fileMetadataService;
    private final FileMetadataMapper fileMetadataMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocument(@RequestPart("file") MultipartFile file,
                                                 @RequestPart("metadata") FileMetadataRequestDto fileMetadata
    ) throws HttpMediaTypeNotSupportedException, IOException {
        String fileId = documentService.uploadDocument(file, fileMetadata);
        return ResponseEntity.ok("Document id: " + fileId);
    }

    @GetMapping(value = "{documentId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String documentId) throws IOException {
        File file = documentService.downloadDocument(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(file.getStream()));
    }

    @GetMapping(value = "{documentId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileMetadataResponseDto> getDocumentMetadata(@PathVariable String documentId) {
        FileMetadata fileMetadata = fileMetadataService.findMetadataByFileId(documentId);
        FileMetadataResponseDto fileMetadataDto = fileMetadataMapper.toDto(fileMetadata);
        return ResponseEntity.ok(fileMetadataDto);
    }

    @PatchMapping(value = "{documentId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDocumentMetadata(@PathVariable String documentId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "id.in", produces = "application/zip")
    public ResponseEntity<Void> downloadDocumentsAsZipFile(@RequestParam("id.in") List<String> documentIds) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
