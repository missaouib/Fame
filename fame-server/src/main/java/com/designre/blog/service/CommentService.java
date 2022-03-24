package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.dto.CommentDto;
import com.designre.blog.model.enums.CommentAssessType;
import com.designre.blog.model.entity.Comment;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;

public interface CommentService extends IService<Comment> {

    void createComment(@NonNull Comment comment);
    Page<Comment> pageByArticleId(Integer current, Integer size, Integer articleId);
    Page<Comment> pageCommentAdmin(Integer current, Integer size);
    CommentDto getCommentDto(Integer id);
    void deleteComment(Integer id);
    int deleteByArticleId(Integer articleId);
    void assessComment(Integer commentId, CommentAssessType assess);
    void createCommentEvent(Comment comment);
    Long countByArticleId(Integer articleId);
    Map<Integer, Long> countByArticleIds(Collection<Integer> articleIds);
}
