package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.entity.Media;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService extends IService<Media> {


    Page<Media> pageAdminMedias(Integer current, Integer size);
    Media getMedia(Integer id);
    Media upload(MultipartFile file, String path);
    void delete(Integer id);
}
