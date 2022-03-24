package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.service.ArticleService;
import com.designre.blog.service.ArticleTagService;
import com.designre.blog.service.TagService;
import com.designre.blog.mapper.ArticleTagMapper;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.ArticleTag;
import com.designre.blog.model.entity.BaseEntity;
import com.designre.blog.model.entity.Tag;
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
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    private final TagService tagService;

    private final ArticleService articleService;

    @Override
    public List<Tag> listTagByArticleId(Integer articleId) {
        Set<Integer> tagIds = lambdaQuery()
                .eq(ArticleTag::getArticleId, articleId)
                .list()
                .stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        return tagService.listByIds(tagIds);
    }

    @Override
    public void deleteByTagId(Integer tagId) {
        lambdaUpdate()
                .eq(ArticleTag::getTagId, tagId)
                .remove();
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        lambdaUpdate()
                .eq(ArticleTag::getArticleId, articleId)
                .remove();
    }

    @Override
    public Map<Integer, List<Article>> listArticleByTagIds(Collection<Integer> tagIds, boolean isFront) {
        List<ArticleTag> articleTags = lambdaQuery()
                .in(ArticleTag::getTagId, tagIds)
                .list();

        if (CollectionUtils.isEmpty(articleTags)) {
            return Collections.emptyMap();
        }

        Set<Integer> articleIds = articleTags.stream()
                .map(ArticleTag::getArticleId)
                .collect(Collectors.toSet());

        Map<Integer, Article> articleMap = articleService.listByIds(articleIds, isFront)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, article -> article, (o1, o2) -> o1));

        return articleTags
                .stream()
                .collect(Collectors.groupingBy(ArticleTag::getTagId,
                        Collectors.mapping(articleTag -> {
                            Integer articleId = articleTag.getArticleId();
                            return articleMap.get(articleId);
                        }, Collectors.toList())));
    }

    @Override
    public Map<Integer, List<Tag>> listTagByArticleIds(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }

        List<ArticleTag> articleTags = lambdaQuery()
                .in(ArticleTag::getArticleId, articleIds)
                .list();

        if (CollectionUtils.isEmpty(articleTags)) {
            return Collections.emptyMap();
        }

        Set<Integer> tagIds = articleTags
                .stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toSet());

        Map<Integer, Tag> tagMap = tagService.listByIds(tagIds)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, o -> o));

        return articleTags
                .stream()
                .collect(Collectors.groupingBy(ArticleTag::getArticleId,
                        Collectors.mapping(articleTag -> tagMap.get(articleTag.getTagId()),
                                Collectors.toList())));
    }

    @Override
    public void createOrUpdate(Integer articleId, Set<Integer> tagIds) {
        Assert.notNull(articleId, "articleId can not be null!");
        lambdaUpdate()
                .eq(ArticleTag::getArticleId, articleId)
                .remove();

        if (CollectionUtils.isEmpty(tagIds)) {
            return;
        }
        Set<ArticleTag> articleTags = tagIds.stream()
                .map(tagId -> {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setArticleId(articleId);
                    articleTag.setTagId(tagId);
                    return articleTag;
                }).collect(Collectors.toSet());
        saveBatch(articleTags);
    }
}
