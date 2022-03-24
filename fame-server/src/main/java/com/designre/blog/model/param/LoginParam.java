package com.designre.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {
    @NotBlank(message = "Username can not be empty")
    private String username;
    @NotBlank(message = "password can not be blank")
    private String password;
}
