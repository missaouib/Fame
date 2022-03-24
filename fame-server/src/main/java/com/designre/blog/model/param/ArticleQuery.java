package com.designre.blog.model.param;

import com.designre.blog.model.enums.ArticleStatus;
import lombok.Data;

@Data
public class ArticleQuery {

    private String title;

    private ArticleStatus status;

    private Integer priority;

    private Boolean listShow;

    private Boolean headerShow;
}
