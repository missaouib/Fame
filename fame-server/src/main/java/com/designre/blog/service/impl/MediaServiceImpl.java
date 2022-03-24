package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.exception.NotFoundException;
import com.designre.blog.exception.TipException;
import com.designre.blog.service.MediaService;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.FameUtils;
import com.designre.blog.mapper.MediaMapper;
import com.designre.blog.model.entity.Media;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Override
    public Page<Media> pageAdminMedias(Integer current, Integer size) {
        Page<Media> page = new Page<>(current, size);
        page.addOrder(OrderItem.desc("id"));
        return page(page);
    }

    @Override
    public Media getMedia(Integer id) {
        Media media = getById(id);
        if (null == media) {
            throw new NotFoundException(Media.class);
        }
        return media;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Media upload(MultipartFile file, String path) {
        if (null == file || file.isEmpty()) {
            throw new TipException("Uploaded file cannot be empty");
        }
        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasText(fileName)) {
            throw new TipException("Uploaded file name cannot be empty");
        }
        String suffix = FameUtils.getFileSuffix(file.getOriginalFilename());
        if (!FameUtils.isImgFile(suffix)) {
            throw new TipException("Wrong media file format");
        }

        Media media = new Media();
        try {
            Path basePath = Paths.get(path);

            Path fameDir = FameUtils.getFameDir();
            Path uploadPath = fameDir.resolve(FameConst.MEDIA_DIR);

            String mediaName = fileName.endsWith(suffix) ? fileName : fileName + "." + suffix;
            Path mediaUrl = basePath.resolve(mediaName);
            Path mediaPath = uploadPath.resolve(mediaUrl);
            log.info("Uploading media: [{}]", mediaPath);

            if (Files.exists(mediaPath)) {
                throw new TipException("A file with the same name already exists!");
            }

            String thumbnailName = fileName.endsWith(suffix) ?
                    FameUtils.getFileBaseName(fileName) + FameConst.MEDIA_THUMBNAIL_SUFFIX + "." + suffix
                    : fileName + FameConst.MEDIA_THUMBNAIL_SUFFIX + "." + suffix;
            Path thumbnailUrl = basePath.resolve(thumbnailName);

            media.setName(mediaName);
            media.setUrl(mediaUrl.toString());
            media.setSuffix(suffix);
            media.setThumbUrl(thumbnailUrl.toString());
            save(media);

            Files.createDirectories(mediaPath.getParent());
            file.transferTo(mediaPath.toFile());

            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                Path thumbnailPath = uploadPath.resolve(thumbnailUrl);
                log.info("Compress media thumbnail: [{}]", thumbnailPath);

                FameUtils.compressImage(mediaPath.toFile(), thumbnailPath.toFile(), 0.5f);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }
        return media;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer id) {
        Media media = getById(id);
        if (null == media) {
            throw new NotFoundException(Media.class);
        }

        removeById(id);

        Path fameDir = FameUtils.getFameDir();
        Path uploadPath = fameDir.resolve(FameConst.MEDIA_DIR);

        Path mediaPath = uploadPath.resolve(media.getUrl());
        if (Files.exists(mediaPath)) {
            try {
                Files.delete(mediaPath);
            } catch (IOException e) {
                throw new TipException(e);
            }
        }

        if (!ObjectUtils.isEmpty(media.getThumbUrl())) {
            Path thumbnailPath = uploadPath.resolve(media.getThumbUrl());
            if (Files.exists(thumbnailPath)) {
                try {
                    Files.delete(thumbnailPath);
                } catch (IOException e) {
                    throw new TipException(e);
                }
            }
        }
    }
}
