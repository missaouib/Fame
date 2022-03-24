package com.designre.blog.model.dto;

import com.designre.blog.model.enums.ArticleStatus;
import com.designre.blog.model.entity.Category;
import com.designre.blog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDetailDto {

    private Integer id;

    private String title;

    private String content;

    private String contentHtml;

    private Integer authorId;

    private Integer hits;

    private ArticleStatus status;

    private boolean listShow;

    private boolean headerShow;

    private Integer priority;

    private Boolean allowComment;

    private Long commentCount;

    private Category category;

    private List<Tag> tags;

    protected Date publishTime;
}
