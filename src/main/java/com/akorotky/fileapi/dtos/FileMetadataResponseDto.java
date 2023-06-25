package com.akorotky.fileapi.dtos;

import com.akorotky.fileapi.constants.DocumentMimeType;
import com.akorotky.fileapi.domain.User;

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
