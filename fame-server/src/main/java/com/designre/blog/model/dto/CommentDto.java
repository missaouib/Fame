package com.designre.blog.model.dto;

import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CommentDto extends Comment {

    private Article article;
    private Comment parentComment;
}
