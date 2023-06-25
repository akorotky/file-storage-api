package com.akorotky.fileapi.controllers;

import com.akorotky.fileapi.dtos.FileMetadataMapper;
import com.akorotky.fileapi.dtos.FileMetadataResponseDto;
import com.akorotky.fileapi.services.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("file-metadata")
@RequiredArgsConstructor
public class FileMetadataController {

    private final FileMetadataService fileMetadataService;
    private final FileMetadataMapper fileMetadataMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<FileMetadataResponseDto>> searchFileMetadata(
            @RequestPart(name = "fileId.in", required = false) List<String> fileIds,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var fileMetadata = fileMetadataService.findAllOrByFileIds(fileIds, pageable).map(fileMetadataMapper::toDto);
        return ResponseEntity.ok(fileMetadata);
    }
}
