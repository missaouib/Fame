package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.enums.LogType;
import com.designre.blog.model.entity.SysLog;

public interface SysLogService extends IService<SysLog> {

    void save(String data, String message, LogType type, String ip, Integer userId);
    Page<SysLog> pageSysLog(Integer current, Integer size);
}
