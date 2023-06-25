package com.akorotky.fileapi.constants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Constants {

    public static final Set<String> allowedDocumentMimeTypes =
            Arrays.stream(DocumentMimeType.class.getEnumConstants())
                    .map(DocumentMimeType::getValue)
                    .collect(Collectors.toSet());
}
