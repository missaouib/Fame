package com.designre.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordParam {
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
}
