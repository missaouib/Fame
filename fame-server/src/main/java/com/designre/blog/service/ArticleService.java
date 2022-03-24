package com.designre.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.dto.ArchiveDto;
import com.designre.blog.model.dto.ArticleDetailDto;
import com.designre.blog.model.dto.ArticleInfoDto;
import com.designre.blog.model.param.ArticleQuery;
import com.designre.blog.model.param.SaveArticleParam;
import com.designre.blog.model.entity.Article;

import java.util.Collection;
import java.util.List;

public interface ArticleService extends IService<Article> {

    IPage<ArticleDetailDto> pageArticleFront(Integer current, Integer size, List<String> sort);
    ArticleDetailDto getArticleFront(Integer id);
    IPage<ArticleDetailDto> pageArticleAdmin(Integer current, Integer size, ArticleQuery query);
    ArticleDetailDto getArticleAdmin(Integer id);
    ArticleDetailDto createOrUpdate(SaveArticleParam param);
    void delete(Integer id);
    void visitArticle(Integer id);
    void increaseHits(Integer id, Integer increaseHits);
    List<ArchiveDto> getArchives();
    List<ArticleInfoDto> listArticleHeader();
    List<Article> listByIds(Collection<Integer> ids, boolean isFront);
}
