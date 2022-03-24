package com.designre.blog.controller.admin;

import com.designre.blog.model.dto.CategoryInfoDto;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.Category;
import com.designre.blog.model.param.SaveCategoryParam;
import com.designre.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public RestResponse<List<CategoryInfoDto>> getAll() {
        List<CategoryInfoDto> categoryInfos = categoryService.listCategoryInfo(false);
        return RestResponse.ok(categoryInfos);
    }

    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return RestResponse.ok();
    }
    @PostMapping
    public RestResponse<Category> save(@RequestBody @Valid SaveCategoryParam param) {
        Category category = categoryService.createOrUpdate(param);
        return RestResponse.ok(category);
    }
}
