package com.designre.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.model.enums.LogType;
import com.designre.blog.service.SysLogService;
import com.designre.blog.mapper.SysLogMapper;
import com.designre.blog.model.entity.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String data, String message, LogType type, String ip, Integer userId) {
        SysLog sysLog = new SysLog();
        sysLog.setData(data);
        sysLog.setMessage(message);
        sysLog.setLogType(type);
        sysLog.setIp(ip);
        sysLog.setUserId(userId);
        this.save(sysLog);
    }

    @Override
    public Page<SysLog> pageSysLog(Integer current, Integer size) {
        Page<SysLog> page = new Page<>(current, size);
        return page(page);
    }
}
