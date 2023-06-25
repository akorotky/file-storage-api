package com.akorotky.fileapi.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentMimeType {

    TXT("text/plain"),
    PDF("application/pdf"),
    JSON("application/json"),
    XML("application/xml"),
    CSV("text/csv"),
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("application/javascript"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


    private final String value;

    @Override
    public String toString() {
        return getValue();
    }

}
