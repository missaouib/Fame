package com.designre.blog.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Comment extends BaseEntity {

    private Integer articleId;
    private Integer parentId;
    private String content;
    private String name;
    private String email;
    private String website;
    private Integer agree;
    private Integer disagree;
    private String ip;
    private String agent;
}
