package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.exception.NotFoundException;
import com.designre.blog.model.dto.ArticleInfoDto;
import com.designre.blog.model.dto.CategoryInfoDto;
import com.designre.blog.service.ArticleCategoryService;
import com.designre.blog.service.CategoryService;
import com.designre.blog.util.FameUtils;
import com.designre.blog.mapper.CategoryMapper;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.BaseEntity;
import com.designre.blog.model.entity.Category;
import com.designre.blog.model.param.SaveCategoryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleCategoryService articleCategoryService;

    @Override
    public void delete(Integer id) {
        recursionDelete(id);
    }

    private void recursionDelete(Integer id) {
        Category category = getById(id);
        if (null == category) {
            return;
        }
        removeById(id);
        articleCategoryService.deleteByCategoryId(id);

        if (null != category.getParentId()) {
            recursionDelete(category.getParentId());
        }
    }

    @Override
    public Category createOrUpdate(SaveCategoryParam param) {
        Integer parentId = param.getParentId();
        if (null != parentId) {
            Optional.ofNullable(getById(parentId))
                    .orElseThrow(() -> new NotFoundException(Category.class));
        }

        Category category = FameUtils.convertTo(param, Category.class);
        saveOrUpdate(category);
        return category;
    }

    @Override
    public List<CategoryInfoDto> listCategoryInfo(boolean isFront) {
        List<Category> categories = list();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        Set<Integer> categoryIds = categories
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        Map<Integer, List<Article>> articleMap = articleCategoryService.listArticleByCategoryIds(categoryIds, isFront);
        Map<Integer, List<Category>> childCategoryMap = categories.stream()
                .filter(category -> null != category.getParentId())
                .collect(Collectors.groupingBy(Category::getParentId));

        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .map(category -> categoryToCategoryInfoDto(category, childCategoryMap, articleMap))
                .collect(Collectors.toList());
    }

    private CategoryInfoDto categoryToCategoryInfoDto(Category category,
                                                      Map<Integer, List<Category>> childCategoryMap,
                                                      Map<Integer, List<Article>> articleMap) {
        CategoryInfoDto dto = new CategoryInfoDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        List<ArticleInfoDto> articleInfos = articleMap.getOrDefault(category.getId(), Collections.emptyList())
                .stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.toList());
        dto.setArticleInfos(articleInfos);

        List<Category> childCategory = childCategoryMap.getOrDefault(category.getId(), Collections.emptyList());
        List<CategoryInfoDto> childCategories = childCategory.stream()
                .map(child -> categoryToCategoryInfoDto(child, childCategoryMap, articleMap))
                .collect(Collectors.toList());
        dto.setChildCategories(childCategories);
        return dto;
    }
}
