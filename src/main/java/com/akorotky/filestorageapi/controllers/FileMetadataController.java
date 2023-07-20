package com.akorotky.filestorageapi.controllers;

import com.akorotky.filestorageapi.dtos.FileMetadataMapper;
import com.akorotky.filestorageapi.dtos.FileMetadataResponseDto;
import com.akorotky.filestorageapi.rest.RestModelAssembler;
import com.akorotky.filestorageapi.services.FileMetadataService;
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
import java.util.Optional;

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
        var fileMetadata = Optional.ofNullable(fileIds)
                .map(ids -> fileMetadataService.findFileMetadataByFileIds(fileIds, pageable))
                .orElseGet(() -> fileMetadataService.findAllFileMetadata(pageable))
                .map(fileMetadataMapper::toDto);
        var fileMetadataRestModel = fileMetadataPagedRestModelAssembler.toModel(fileMetadata, fileMetadataRestModelAssembler);
        return ResponseEntity.ok(fileMetadataRestModel);
    }

    @GetMapping(path = "{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<FileMetadataResponseDto>> getFileMetadata(@PathVariable String fileId) {
        return fileMetadataService.findMetadataByFileId(fileId)
                .map(fileMetadataMapper::toDto)
                .map(fileMetadataRestModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
