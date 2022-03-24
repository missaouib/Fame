package com.designre.blog.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.designre.blog.exception.NotFoundException;
import com.designre.blog.exception.TipException;
import com.designre.blog.service.ArticleService;
import com.designre.blog.service.BackupService;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.FameUtils;
import com.designre.blog.model.entity.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class BackupServiceImpl implements BackupService {

    private final ArticleService articleService;

    @Transactional
    @Override
    public void importArticle(MultipartFile file, Integer articleId) {

        if (file.isEmpty()) {
            throw new TipException("Uploaded file cannot be empty ");
        }


        Article article = articleService.getById(articleId);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }


        String fileBaseName = FameUtils.getFileBaseName(file.getOriginalFilename());
        article.setTitle(fileBaseName);

        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            IoUtil.readUtf8Lines(inputStream, lines);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }

        handleArticleContentLines(fileBaseName, lines);

        StringJoiner content = new StringJoiner("\n");
        lines.forEach(content::add);

        if (!StringUtils.hasText(content.toString())) {
            throw new TipException("Imported file content is empty");
        }

        article.setContent(content.toString());

        articleService.updateById(article);
    }

    private void handleArticleContentLines(String filename, List<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            return;
        }

        if (isTitleLine(filename, lines.get(0))) {
            lines.remove(0);
            Iterator<String> it = lines.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!StrUtil.isEmpty(next)) {
                    break;

                }
                it.remove();
            }
        }
    }

    @Override
    public Resource exportArticle(Integer articleId) {
        Article article = articleService.getById(articleId);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }

        try {
            Path filePath = Paths.get(FameConst.BACKUP_DIR, article.getTitle() + FameConst.MARKDOWN_SUFFIX);
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }


            try (final BufferedWriter writer = FileUtil.getWriter(filePath.toFile(), CharsetUtil.UTF_8, false)) {
                writer.write(article.getContent());
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }


        throw new TipException("Export file exception");
    }

    private boolean isTitleLine(String title, String line) {
        String condition1 = "# " + title;
        String condition2 = "#  " + title;
        return StrUtil.equalsAny(line, condition1, condition2);
    }
}
