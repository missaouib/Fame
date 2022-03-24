package com.designre.blog.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.designre.blog.model.dto.CommentDto;
import com.designre.blog.model.dto.Pagination;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.Comment;
import com.designre.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/comment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public RestResponse<Pagination<Comment>> index(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                   @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Comment> comments = commentService.pageCommentAdmin(page, limit);
        return RestResponse.ok(Pagination.of(comments));
    }

    @GetMapping("{id}")
    public RestResponse<Comment> detail(@PathVariable Integer id) {
        CommentDto comment = commentService.getCommentDto(id);
        if (null != comment.getParentComment()) {
            comment.getParentComment().setContent(FameUtils.mdToHtml(comment.getParentComment().getContent()));
        }
        comment.setContent(FameUtils.mdToHtml(comment.getContent()));
        return RestResponse.ok(comment);
    }

    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return RestResponse.ok();
    }

    @GetMapping("count")
    public RestResponse<Long> count() {
        return RestResponse.ok(commentService.count());
    }

}
