package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.exception.TipException;
import com.designre.blog.model.dto.ArticleInfoDto;
import com.designre.blog.model.dto.TagInfoDto;
import com.designre.blog.util.FameUtils;
import com.designre.blog.mapper.TagMapper;
import com.designre.blog.model.entity.Article;
import com.designre.blog.model.entity.BaseEntity;
import com.designre.blog.model.entity.Tag;
import com.designre.blog.model.param.SaveTagParam;
import com.designre.blog.service.ArticleTagService;
import com.designre.blog.service.TagService;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ArticleTagService articleTagService;

    @Override
    public void delete(Integer id) {
        if (!removeById(id)) {
            throw new TipException("Failed to delete tag");
        }
        articleTagService.deleteByTagId(id);
    }

    @Override
    public Tag createOrUpdate(SaveTagParam param) {
        Tag tag = FameUtils.convertTo(param, Tag.class);
        saveOrUpdate(tag);
        return tag;
    }

    @Override
    public List<TagInfoDto> listTagInfo(boolean isFront) {
        List<Tag> tags = list();
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }
        Set<Integer> tagIds = tags
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        Map<Integer, List<Article>> articleMap = articleTagService.listArticleByTagIds(tagIds, isFront);

        return tags.stream()
                .map(tag -> {
                    TagInfoDto dto = new TagInfoDto();
                    dto.setId(tag.getId());
                    dto.setName(tag.getName());

                    List<ArticleInfoDto> articleInfoDtos = articleMap.getOrDefault(tag.getId(), Collections.emptyList())
                            .stream()
                            .map(ArticleInfoDto::new)
                            .sorted((Comparator.comparing(ArticleInfoDto::getPublishTime).reversed()))
                            .collect(Collectors.toList());
                    dto.setArticleInfos(articleInfoDtos);
                    return dto;
                }).collect(Collectors.toList());
    }
}
