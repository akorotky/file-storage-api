package com.akorotky.fileapi.rest.impl;

import com.akorotky.fileapi.controllers.DocumentController;
import com.akorotky.fileapi.controllers.FileMetadataController;
import com.akorotky.fileapi.dtos.FileMetadataResponseDto;
import com.akorotky.fileapi.rest.RestModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FileMetadataModelAssembler implements RestModelAssembler<FileMetadataResponseDto> {

    @Override
    public @NonNull EntityModel<FileMetadataResponseDto> toModel(@NonNull FileMetadataResponseDto entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(FileMetadataController.class).getFileMetadata(entity.fileId())).withSelfRel(),
                    linkTo(methodOn(DocumentController.class).downloadDocument(entity.fileId())).withRel("file"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
