package com.akorotky.filestorageapi.controllers;

import com.akorotky.filestorageapi.domain.File;
import com.akorotky.filestorageapi.dtos.FileMetadataMapper;
import com.akorotky.filestorageapi.dtos.FileMetadataRequestDto;
import com.akorotky.filestorageapi.dtos.FileMetadataResponseDto;
import com.akorotky.filestorageapi.services.FileService;
import com.akorotky.filestorageapi.services.FileMetadataService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileService fileService;
    private final FileMetadataService fileMetadataService;
    private final FileMetadataMapper fileMetadataMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @RequestPart("metadata") FileMetadataRequestDto fileMetadata
    ) throws HttpMediaTypeNotSupportedException, IOException {
        String fileId = fileService.uploadFile(file, fileMetadata);
        return ResponseEntity.ok("File id: " + fileId);
    }

    @GetMapping(value = "{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable String fileId) throws IOException {
        File file = fileService.downloadFile(fileId);
        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = file.getStream()) {
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(responseBody);
    }

    @GetMapping(value = "{fileId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileMetadataResponseDto> getFileMetadata(@PathVariable String fileId) {
        return fileMetadataService.findMetadataByFileId(fileId)
                .map(fileMetadataMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "{fileId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFileMetadata(@PathVariable String fileId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "id.in", produces = "application/zip")
    public void downloadFilesAsZipFile(
            @RequestParam(name = "id.in") @Size(min = 1, max = 80) List<String> fileIds,
            HttpServletResponse response
    ) throws IOException {
        // must set the headers before writing to the response's output stream
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip");
        fileService.downloadFilesAsZip(fileIds, response);
    }

    @DeleteMapping(value = "{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
