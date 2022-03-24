package com.designre.blog.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Media extends BaseEntity {


    private String name;
    private String url;
    private String thumbUrl;
    private String suffix;
}
