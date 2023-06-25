package com.akorotky.fileapi.controllers;

import com.akorotky.fileapi.domain.File;
import com.akorotky.fileapi.dtos.FileMetadataRequestDto;
import com.akorotky.fileapi.services.DocumentService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") FileMetadataRequestDto fileMetadata
    ) throws HttpMediaTypeNotSupportedException, IOException {
        String fileId = documentService.uploadDocument(file, fileMetadata);
        return ResponseEntity.ok("FileId: " + fileId);
    }

    @GetMapping(value = "{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable String filename) throws IOException {
        File file = documentService.downloadDocument(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(file.getStream()));
    }

    @GetMapping(value = "{filename}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getDocumentMetadata(@PathVariable String filename) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "{filename}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateDocumentMetadata(@PathVariable String filename) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "filename.in", produces = "application/zip")
    public ResponseEntity<Void> downloadDocumentsAsZipFile(@RequestParam("filename.in") List<String> filenames) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "{filename}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String filename) {
        return ResponseEntity.noContent().build();
    }
}
