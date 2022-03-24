package com.designre.blog.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class RefreshTokenParam {
    @NotBlank
    private String refreshToken;
}
