package com.designre.blog.model.entity;

import com.designre.blog.model.enums.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysLog extends BaseEntity {

    private String data;
    private String message;
    private LogType logType;
    private String ip;
    private Integer userId;
}
