package com.designre.blog.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArchiveDto {

    private String year;

    private List<ArticleInfoDto> articleInfos;
}
