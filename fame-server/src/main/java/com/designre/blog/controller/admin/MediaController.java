package com.designre.blog.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.designre.blog.model.dto.Pagination;
import com.designre.blog.util.FameConst;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.Media;
import com.designre.blog.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MediaController {

    private final MediaService mediaService;
    @GetMapping
    public RestResponse<Pagination<Media>> index(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                 @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Media> medias = mediaService.pageAdminMedias(page, limit);
        return RestResponse.ok(Pagination.of(medias));
    }

    @GetMapping("{id}")
    public RestResponse<Media> detail(@PathVariable Integer id) {
        Media media = mediaService.getMedia(id);
        return RestResponse.ok(media);
    }

    @PostMapping("upload")
    public RestResponse<Media> upload(@RequestPart("file") MultipartFile file,
                                      @RequestParam String path) {
        Media media = mediaService.upload(file, path);
        return RestResponse.ok(media);
    }

    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        mediaService.delete(id);
        return RestResponse.ok();
    }
}
