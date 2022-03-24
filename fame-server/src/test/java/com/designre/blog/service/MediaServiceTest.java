package com.designre.blog.service;

import com.designre.blog.BaseTest;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.FameUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zzzzbw
 * @since 2019/7/9 17:58
 */
@Slf4j
public class MediaServiceTest extends BaseTest {

    @Autowired
    private MediaService mediaService;

    @Test
    public void test1() {
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return "abc.txt";
            }

            @Override
            public String getContentType() {
                return "123123";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        mediaService.upload(file, "a/b/c");
    }
}
