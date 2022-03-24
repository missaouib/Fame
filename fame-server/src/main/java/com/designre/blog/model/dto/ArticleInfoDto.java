package com.designre.blog.model.dto;

import com.designre.blog.model.entity.Article;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleInfoDto {

    public ArticleInfoDto() {
    }

    public ArticleInfoDto(Article article) {
        super();
        this.id = article.getId();
        this.title = article.getTitle();
        this.publishTime = article.getPublishTime();
    }


    private Integer id;
    private String title;
    private Date publishTime;
}
