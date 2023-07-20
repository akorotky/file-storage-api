package com.akorotky.filestorageapi.domain;

import lombok.Data;

import java.io.InputStream;

@Data
public class File {
    private String filename;
    private InputStream stream;
}
