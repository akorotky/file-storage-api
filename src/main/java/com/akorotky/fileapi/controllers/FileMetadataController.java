package com.akorotky.fileapi.controllers;

import com.akorotky.fileapi.domain.FileMetadata;
import com.akorotky.fileapi.dtos.FileMetadataMapper;
import com.akorotky.fileapi.dtos.FileMetadataResponseDto;
import com.akorotky.fileapi.rest.RestModelAssembler;
import com.akorotky.fileapi.services.FileMetadataService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("file-metadata")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FileMetadataController {

    private final FileMetadataService fileMetadataService;
    private final FileMetadataMapper fileMetadataMapper;
    private final RestModelAssembler<FileMetadataResponseDto> fileMetadataRestModelAssembler;
    private final PagedResourcesAssembler<FileMetadataResponseDto> fileMetadataPagedRestModelAssembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<EntityModel<FileMetadataResponseDto>>> searchFileMetadata(
            @RequestParam(name = "fileId.in", required = false) @Size(min = 2, max = 50) List<String> fileIds,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        var fileMetadata = fileMetadataService.findAllOrByFileIds(fileIds, pageable).map(fileMetadataMapper::toDto);
        var fileMetadataModels = fileMetadataPagedRestModelAssembler.toModel(fileMetadata, fileMetadataRestModelAssembler);
        return ResponseEntity.ok(fileMetadataModels);
    }

    @GetMapping(path = "{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<FileMetadataResponseDto>> getFileMetadata(@PathVariable String fileId) {
        FileMetadata fileMetadata = fileMetadataService.findMetadataByFileId(fileId);
        var fileMetadataDto = fileMetadataMapper.toDto(fileMetadata);
        var fileMetadataModels = fileMetadataRestModelAssembler.toModel(fileMetadataDto);
        return ResponseEntity.ok(fileMetadataModels);
    }
}
