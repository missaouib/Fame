package com.designre.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    protected Integer id;

    protected Date created = new Date();

    @TableField(value = "modified", update = "now()")
    protected Date modified = new Date();

    @TableLogic
    protected Integer deleted = 0;
}
