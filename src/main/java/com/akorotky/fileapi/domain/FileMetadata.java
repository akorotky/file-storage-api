package com.akorotky.fileapi.domain;

import com.akorotky.fileapi.constants.DocumentMimeType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
public class FileMetadata {

    @Id
    private String id;

    private String filename;

    private long fileSize;

    private DocumentMimeType fileType;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    private User user;
}
