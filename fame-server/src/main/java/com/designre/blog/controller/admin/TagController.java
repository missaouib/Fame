package com.designre.blog.controller.admin;

import com.designre.blog.model.dto.TagInfoDto;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.Tag;
import com.designre.blog.model.param.SaveTagParam;
import com.designre.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TagController {

    private final TagService tagService;

    @GetMapping
    public RestResponse<List<TagInfoDto>> getAll() {
        List<TagInfoDto> tagInfos = tagService.listTagInfo(false);
        return RestResponse.ok(tagInfos);
    }

    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        tagService.delete(id);
        return RestResponse.ok();
    }

    @PostMapping
    public RestResponse<Tag> save(@RequestBody @Valid SaveTagParam param) {
        Tag tag = tagService.createOrUpdate(param);
        return RestResponse.ok(tag);
    }
}
