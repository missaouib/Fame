package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.entity.SysOption;

import java.util.Map;

public interface SysOptionService extends IService<SysOption> {
    Map<String, String> getAllOptionMap();
    <T> T get(String key, T defaultValue);
    String get(String key);
    void save(String key, String value);
    void save(Map<String, String> options);
    Map<String, String> getFrontOptionMap();
}
