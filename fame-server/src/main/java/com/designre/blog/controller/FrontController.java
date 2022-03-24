package com.designre.blog.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.designre.blog.model.dto.*;
import com.designre.blog.model.enums.CommentAssessType;
import com.designre.blog.model.param.AddCommentParam;
import com.designre.blog.service.*;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FrontController {
    private final ArticleService articleService;

    private final CategoryService categoryService;

    private final TagService tagService;

    private final CommentService commentService;

    private final SysOptionService sysOptionService;

    @GetMapping("article")
    public RestResponse<Pagination<ArticleDetailDto>> home(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                           @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit,
                                                           @RequestParam(required = false, defaultValue = "id") List<String> sort) {
        IPage<ArticleDetailDto> articles = articleService.pageArticleFront(page, limit, sort);
        return RestResponse.ok(Pagination.of(articles));
    }

    @GetMapping("article/{id}")
    public RestResponse<ArticleDetailDto> post(@PathVariable Integer id) {
        ArticleDetailDto articleDetailDto = articleService.getArticleFront(id);
        articleService.visitArticle(articleDetailDto.getId());
        return RestResponse.ok(articleDetailDto);
    }

    @GetMapping("tag")
    public RestResponse<List<TagInfoDto>> tag() {
        List<TagInfoDto> tagInfos = tagService.listTagInfo(true);
        return RestResponse.ok(tagInfos);
    }

    @GetMapping("category")
    public RestResponse<List<CategoryInfoDto>> category() {
        List<CategoryInfoDto> categoryInfos = categoryService.listCategoryInfo(true);
        return RestResponse.ok(categoryInfos);
    }

    @GetMapping("archive")
    public RestResponse<List<ArchiveDto>> archive() {
        List<ArchiveDto> archives = articleService.getArchives();
        return RestResponse.ok(archives);
    }

    @GetMapping("header")
    public RestResponse<List<ArticleInfoDto>> headerList() {
        List<ArticleInfoDto> articleHeader = articleService.listArticleHeader();
        return RestResponse.ok(articleHeader);
    }

    @GetMapping("comment")
    public RestResponse<Pagination<Comment>> getArticleComment(@RequestParam Integer articleId, @RequestParam(required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.pageByArticleId(page, limit, articleId);
        return RestResponse.ok(Pagination.of(comments));
    }

    @PostMapping("comment")
    public RestResponse<RestResponse.Empty> addComment(@RequestBody @Valid AddCommentParam param) {
        Comment comment = FameUtils.convertTo(param, Comment.class);
        commentService.createComment(comment);
        return RestResponse.ok();
    }

    @PostMapping("comment/agree/{commentId}")
    public RestResponse<RestResponse.Empty> agreeComment(@PathVariable Integer commentId) {
        commentService.assessComment(commentId, CommentAssessType.AGREE);
        return RestResponse.ok();
    }

    @PostMapping("comment/disagree/{commentId}")
    public RestResponse<RestResponse.Empty> disagreeComment(@PathVariable Integer commentId) {
        commentService.assessComment(commentId, CommentAssessType.DISAGREE);
        return RestResponse.ok();
    }

    @GetMapping("option")
    public RestResponse<Map<String, String>> getOption() {
        Map<String, String> map = sysOptionService.getFrontOptionMap();
        return RestResponse.ok(map);
    }
}
