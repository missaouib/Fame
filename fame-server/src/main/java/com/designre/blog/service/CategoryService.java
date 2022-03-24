package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.dto.CategoryInfoDto;
import com.designre.blog.model.entity.Category;
import com.designre.blog.model.param.SaveCategoryParam;

import java.util.List;

public interface CategoryService extends IService<Category> {

    void delete(Integer id);
    Category createOrUpdate(SaveCategoryParam param);
    List<CategoryInfoDto> listCategoryInfo(boolean isFront);
}
