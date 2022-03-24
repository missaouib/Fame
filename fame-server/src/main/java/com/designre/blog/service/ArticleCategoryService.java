package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.ArticleCategory;
import com.designre.blog.model.entity.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ArticleCategoryService extends IService<ArticleCategory> {

    Category getCategoryByArticleId(Integer articleId);
    void deleteByCategoryId(Integer categoryId);
    void deleteByArticleId(Integer articleId);
    Map<Integer, List<Article>> listArticleByCategoryIds(Set<Integer> categoryIds, boolean isFront);
    Map<Integer, Category> listCategoryByArticleIds(Collection<Integer> articleIds);
    void createOrUpdate(Integer articleId, Integer categoryId);
}
