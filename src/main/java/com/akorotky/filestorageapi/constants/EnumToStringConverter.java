package com.akorotky.filestorageapi.constants;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

@WritingConverter
public class EnumToStringConverter<E extends Enum<E>> implements Converter<E, String> {

    @Override
    public String convert(@NonNull E source) {
        return source.toString();
    }
}
