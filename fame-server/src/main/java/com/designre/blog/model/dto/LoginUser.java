package com.designre.blog.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginUser {

    private Integer id;
    private String username;
    private String email;
    private String screenName;
    private Date logged;
    private String token;
}
