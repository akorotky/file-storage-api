package com.akorotky.filestorageapi.rest.impl;

import com.akorotky.filestorageapi.controllers.FileController;
import com.akorotky.filestorageapi.controllers.FileMetadataController;
import com.akorotky.filestorageapi.dtos.FileMetadataResponseDto;
import com.akorotky.filestorageapi.rest.RestModelAssembler;
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
                    linkTo(methodOn(FileController.class).downloadFile(entity.fileId())).withRel("file"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
