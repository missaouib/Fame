package com.designre.blog.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysOption extends BaseEntity {

    private String optionKey;
    private String optionValue;
}
