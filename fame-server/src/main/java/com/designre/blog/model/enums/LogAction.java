package com.designre.blog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogAction {
    SELECT("Select"),

    ADD("Add"),

    UPDATE("Update"),

    DELETE("Delete"),

    SUCCESS("Successful operation"),

    FAIL("Operation failed"),
    ;

    private final String msg;
}
