package com.designre.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.designre.blog.model.dto.ArticleDetailDto;
import com.designre.blog.model.dto.Pagination;
import com.designre.blog.model.param.ArticleQuery;
import com.designre.blog.model.param.SaveArticleParam;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.RestResponse;
import com.designre.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/article")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public RestResponse<Pagination<ArticleDetailDto>> page(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                           @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit,
                                                           ArticleQuery query) {
        IPage<ArticleDetailDto> articles = articleService.pageArticleAdmin(page, limit, query);
        return RestResponse.ok(Pagination.of(articles));
    }

    @GetMapping("{id}")
    public RestResponse<ArticleDetailDto> get(@PathVariable Integer id) {
        ArticleDetailDto articleDetailDto = articleService.getArticleAdmin(id);
        return RestResponse.ok(articleDetailDto);
    }

    @PostMapping
    public RestResponse<ArticleDetailDto> createOrUpdate(@RequestBody @Valid SaveArticleParam param) {
        ArticleDetailDto articleDetailDto = articleService.createOrUpdate(param);
        return RestResponse.ok(articleDetailDto);
    }

    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        articleService.delete(id);
        return RestResponse.ok();
    }

    @GetMapping("count")
    public RestResponse<Long> count() {
        return RestResponse.ok(articleService.count());
    }
}
