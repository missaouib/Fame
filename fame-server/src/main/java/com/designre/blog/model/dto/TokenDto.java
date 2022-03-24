package com.designre.blog.model.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String token;

    private String refreshToken;
}
