package com.designre.blog.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategory extends BaseEntity {


    private Integer articleId;
    private Integer categoryId;
}
