package com.designre.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveCategoryParam {

    private Integer id;

    private Integer parentId;

    @NotBlank(message = "Name is not allowed to be empty")
    private String name;
}
