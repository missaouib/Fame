package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.ArticleTag;
import com.designre.blog.model.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleTagService extends IService<ArticleTag> {

    List<Tag> listTagByArticleId(Integer articleId);
    void deleteByTagId(Integer tagId);
    void deleteByArticleId(Integer articleId);
    Map<Integer, List<Article>> listArticleByTagIds(Collection<Integer> tagIds, boolean isFront);
    Map<Integer, List<Tag>> listTagByArticleIds(Collection<Integer> articleIds);
    void createOrUpdate(Integer articleId, Set<Integer> tagIds);
}
