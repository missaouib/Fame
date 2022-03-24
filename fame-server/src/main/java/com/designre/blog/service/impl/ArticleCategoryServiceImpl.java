package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.service.ArticleCategoryService;
import com.designre.blog.service.ArticleService;
import com.designre.blog.service.CategoryService;
import com.designre.blog.mapper.ArticleCategoryMapper;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.ArticleCategory;
import com.designre.blog.model.entity.BaseEntity;
import com.designre.blog.model.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    private final CategoryService categoryService;

    private final ArticleService articleService;

    @Override
    public Category getCategoryByArticleId(Integer articleId) {
        ArticleCategory articleCategory = lambdaQuery()
                .eq(ArticleCategory::getArticleId, articleId)
                .one();
        if (null == articleCategory) {
            return null;
        }
        return categoryService.getById(articleCategory.getCategoryId());
    }

    @Override
    public void deleteByCategoryId(Integer categoryId) {
        lambdaUpdate()
                .eq(ArticleCategory::getCategoryId, categoryId)
                .remove();
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        lambdaUpdate()
                .eq(ArticleCategory::getArticleId, articleId)
                .remove();
    }

    @Override
    public Map<Integer, List<Article>> listArticleByCategoryIds(Set<Integer> categoryIds, boolean isFront) {
        List<ArticleCategory> articleCategories = lambdaQuery()
                .in(ArticleCategory::getCategoryId, categoryIds)
                .list();

        if (CollectionUtils.isEmpty(articleCategories)) {
            return Collections.emptyMap();
        }

        Set<Integer> articleIds = articleCategories.stream()
                .map(ArticleCategory::getArticleId)
                .collect(Collectors.toSet());

        Map<Integer, Article> articleMap = articleService.listByIds(articleIds, isFront)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, article -> article, (o1, o2) -> o1));

        return articleCategories
                .stream()
                .collect(Collectors.groupingBy(ArticleCategory::getCategoryId,
                        Collectors.mapping(articleCategory -> {
                            Integer articleId = articleCategory.getArticleId();
                            return articleMap.get(articleId);
                        }, Collectors.toList())));
    }

    @Override
    public Map<Integer, Category> listCategoryByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        List<ArticleCategory> articleCategories = lambdaQuery()
                .in(ArticleCategory::getArticleId, articleIds)
                .list();

        if (CollectionUtils.isEmpty(articleCategories)) {
            return Collections.emptyMap();
        }

        Set<Integer> categoryIds = articleCategories
                .stream()
                .map(ArticleCategory::getCategoryId)
                .collect(Collectors.toSet());

        Map<Integer, Category> categoryMap = categoryService.listByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, o -> o));

        Map<Integer, Category> map = new HashMap<>();
        for (ArticleCategory o : articleCategories) {
            if (map.put(o.getArticleId(), categoryMap.get(o.getCategoryId())) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        return map;
    }

    @Override
    public void createOrUpdate(Integer articleId, Integer categoryId) {
        Assert.notNull(articleId, "articleId can not be null!");

        lambdaUpdate()
                .eq(ArticleCategory::getArticleId, articleId)
                .remove();

        if (categoryId == null) {
            return;
        }
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleId(articleId);
        articleCategory.setCategoryId(categoryId);
        save(articleCategory);

    }
}
