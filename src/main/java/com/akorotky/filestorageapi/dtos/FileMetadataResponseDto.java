package com.akorotky.filestorageapi.dtos;

import com.akorotky.filestorageapi.constants.DocumentMimeType;
import com.akorotky.filestorageapi.domain.User;

import java.time.Instant;

public record FileMetadataResponseDto(
        String id,

        String fileId,

        String filename,

        long fileSize,

        DocumentMimeType fileType,

        Instant createdDate,

        Instant updatedDate,

        User user
) {
}
