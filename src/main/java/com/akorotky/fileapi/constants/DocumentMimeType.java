package com.akorotky.fileapi.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

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

    @JsonValue
    @Override
    public String toString() {
        return getValue();
    }

    @ReadingConverter
    public static class StringToEnumConverter implements Converter<String, DocumentMimeType> {
        @Override
        public DocumentMimeType convert(@NonNull String source) {
            return EnumUtils.fromValue(DocumentMimeType.class, source);
        }
    }
}
