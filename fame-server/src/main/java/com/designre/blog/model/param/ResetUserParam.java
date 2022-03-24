package com.designre.blog.model.param;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetUserParam {
    @NotBlank(message = "Username can not be empty")
    private String username;

    @NotBlank(message = "E-mail can not be empty")
    @Email(message = "Email format error")
    private String email;
}
