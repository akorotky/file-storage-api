package com.akorotky.filestorageapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private final long id;
    private String username;
    private String email;
}