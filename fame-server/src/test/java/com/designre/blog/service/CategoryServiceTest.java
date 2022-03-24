package com.designre.blog.service;

import com.designre.blog.model.dto.CategoryInfoDto;
import com.designre.blog.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/3/22 20:49
 */
@Slf4j
public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void listCategoryInfo() {
        List<CategoryInfoDto> categoryInfoDtos = categoryService.listCategoryInfo(false);
        log.info("{}", categoryInfoDtos);
    }
}
