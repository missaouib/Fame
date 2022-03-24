package com.designre.blog.model.param;

import com.designre.blog.model.enums.ArticleStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author by zzzzbw
 * @since 2021/01/20 10:57
 */
@Data
public class SaveNoteParam {

    private Integer id;

    @NotBlank(message = "Title is not allowed to be empty")
    private String title;

    @NotBlank(message = "Content not allowed to be empty")
    private String content;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Integer priority = 0;

    private Boolean allowComment = false;
}
