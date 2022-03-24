package com.designre.blog.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {

    private String username;
    private String passwordMd5;
    private String email;
    private String screenName;
    private Date logged;
}
