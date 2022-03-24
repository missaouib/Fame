package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.dto.TagInfoDto;
import com.designre.blog.model.entity.Tag;
import com.designre.blog.model.param.SaveTagParam;

import java.util.List;

public interface TagService extends IService<Tag> {
    void delete(Integer id);
    Tag createOrUpdate(SaveTagParam param);
    List<TagInfoDto> listTagInfo(boolean isFront);
}
