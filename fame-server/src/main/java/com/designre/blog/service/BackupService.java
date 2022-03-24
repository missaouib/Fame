package com.designre.blog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BackupService {
    void importArticle(MultipartFile file, Integer articleId);

    Resource exportArticle(Integer articleId);
}
