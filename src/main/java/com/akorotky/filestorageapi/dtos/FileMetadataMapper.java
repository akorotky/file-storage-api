package com.akorotky.filestorageapi.dtos;

import com.akorotky.filestorageapi.domain.FileMetadata;
import com.akorotky.filestorageapi.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userEmail", target = "email")
    User toUser(FileMetadataRequestDto fileMetadataRequestDto);

    FileMetadataResponseDto toDto(FileMetadata fileMetadata);
}
