package com.akorotky.fileapi.dtos;

import com.akorotky.fileapi.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    User toUser(FileMetadataRequestDto fileMetadataRequestDto);
}
