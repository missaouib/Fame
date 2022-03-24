package com.designre.blog.model.entity;

import com.designre.blog.model.enums.ArticleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Article extends BaseEntity {

    private String title;
    private String content;
    private Integer authorId;
    private Integer hits;
    private ArticleStatus status;
    private boolean listShow;
    private boolean headerShow;
    private Integer priority;
    private Boolean allowComment;
    protected Date publishTime;
}
