package com.designre.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class CategoryInfoDto {


    private Integer id;

    private String name;
    
    private List<CategoryInfoDto> childCategories;

    @JsonInclude
    private List<ArticleInfoDto> articleInfos;

}
