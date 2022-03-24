package com.designre.blog.model.param;

import com.designre.blog.model.enums.ArticleStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class SaveArticleParam {

    private Integer id;

    @NotBlank(message = "Title is not allowed to be empty")
    private String title;

    @NotBlank(message = "Content not allowed to be empty")
    private String content;

    private Set<Integer> tagIds;

    private Integer categoryId;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Boolean listShow = false;

    private Boolean headerShow = false;

    private Integer priority = 0;

    private Boolean allowComment = false;

    @NotNull(message = "Publish time cannot be empty")
    private Date publishTime;

}
