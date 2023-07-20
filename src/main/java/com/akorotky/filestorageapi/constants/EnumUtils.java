package com.akorotky.filestorageapi.constants;

public class EnumUtils {

    public static <E extends Enum<E>> E fromValue(Class<E> enumClass, String value) {
        if (value == null) return null;
        for (E enumType : enumClass.getEnumConstants()) {
            if (enumType.toString().equals(value)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
