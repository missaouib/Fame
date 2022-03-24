package com.designre.blog.listener;

import com.designre.blog.exception.TipException;
import com.designre.blog.listener.event.LogEvent;
import com.designre.blog.model.enums.LogAction;
import com.designre.blog.service.SysLogService;
import com.designre.blog.util.FameUtils;
import com.designre.blog.model.entity.BaseEntity;
import com.designre.blog.model.entity.SysLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogEventListener {


    private final SysLogService sysLogService;


    @Async
    @EventListener
    public void onLogEvent(LogEvent event) {
        log.info("onLogEvent event:{}", event);

        SysLog sysLog = createLog(event);
        sysLogService.save(sysLog);
    }

    private SysLog createLog(LogEvent event) {
        SysLog sysLog = new SysLog();
        LogAction action = event.getAction();
        String logData = createLogData(event.getData());
        sysLog.setData(logData);
        sysLog.setMessage(action.getMsg());
        sysLog.setLogType(event.getType());
        sysLog.setIp(event.getIp());
        sysLog.setUserId(event.getUserId());
        return sysLog;
    }

    private String createLogData(Object data) {
        if (Objects.isNull(data)) {
            return String.valueOf((Object) null);
        }
        if (data instanceof String) {
            return (String) data;
        } else if (data instanceof Collection) {
            if (ObjectUtils.isEmpty(data)) {
                throw new TipException("Log data is empty");
            }
            Object next = ((Collection<?>) data).iterator().next();
            if (next instanceof BaseEntity) {
                @SuppressWarnings("unchecked")
                Collection<? extends BaseEntity> list = (Collection<? extends BaseEntity>) data;
                return "ids:" + list.stream().map(BaseEntity::getId).map(String::valueOf).collect(Collectors.joining(","));
            }
        }

        return FameUtils.objectToJson(data);
    }

}
