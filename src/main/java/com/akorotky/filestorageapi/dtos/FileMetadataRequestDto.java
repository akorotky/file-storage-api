package com.akorotky.filestorageapi.dtos;

public record FileMetadataRequestDto(
        Long userId,
        String userEmail,
        String username
) {
}
