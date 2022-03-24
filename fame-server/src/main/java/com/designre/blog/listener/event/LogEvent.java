package com.designre.blog.listener.event;

import com.designre.blog.model.enums.LogAction;
import com.designre.blog.model.enums.LogType;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Getter
public class LogEvent extends ApplicationEvent {

    private final Object data;

    private final LogAction action;

    private final LogType type;

    private final String ip;

    private final Integer userId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param data   Data required for the log
     * @param action log operation
     * @param type   log type
     * @param ip     ip
     * @param userId  user id
     */
    public LogEvent(Object source, Object data, @NonNull LogAction action, @NonNull LogType type, String ip, Integer userId) {
        super(source);
        this.data = data;
        this.action = action;
        this.type = type;
        this.ip = ip;
        this.userId = userId;
    }

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param data   日志所需的数据
     * @param action 日志操作
     * @param type   日志类型
     */
    public LogEvent(Object source, Object data, LogAction action, LogType type) {
        this(source, data, action, type, null, null);
    }
}
