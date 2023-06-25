package com.akorotky.fileapi.dtos;

import com.akorotky.fileapi.domain.FileMetadata;
import com.akorotky.fileapi.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userEmail", target = "email")
    User toUser(FileMetadataRequestDto fileMetadataRequestDto);

    FileMetadataResponseDto toDto(FileMetadata fileMetadata);
}
