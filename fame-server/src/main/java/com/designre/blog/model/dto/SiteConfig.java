package com.designre.blog.model.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SiteConfig {

    private String title;
    private String description;
    private String keywords;
    private boolean emailSend;
    private String emailHost;
    private Integer emailPort;
    private String emailUsername;
    private String emailPassword;
}
