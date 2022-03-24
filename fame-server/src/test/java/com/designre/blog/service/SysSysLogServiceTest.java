package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.designre.blog.util.FameUtils;
import com.designre.blog.BaseTest;
import com.designre.blog.model.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzzzbw
 * @since 2019/6/24 13:54
 */
@Slf4j
public class SysSysLogServiceTest extends BaseTest {

    @Autowired
    private SysLogService sysLogService;

    @Test
    public void pageSysLog() {
        Page<SysLog> page = sysLogService.pageSysLog(0, 10);
        log.info("{}", FameUtils.objectToJson(page));
    }

}
