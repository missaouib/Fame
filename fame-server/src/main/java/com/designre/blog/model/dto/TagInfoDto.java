package com.designre.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class TagInfoDto {

    private Integer id;
    private String name;
    @JsonInclude
    private List<ArticleInfoDto> articleInfos;
}
