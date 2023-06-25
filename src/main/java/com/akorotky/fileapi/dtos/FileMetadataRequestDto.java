package com.akorotky.fileapi.dtos;

public record FileMetadataRequestDto(
        Long userId,
        String userEmail,
        String username
) {
}
