create table if not exists SYS_OPTION
(
    ID           INT auto_increment
        primary key comment 'ID',
    OPTION_KEY   VARCHAR(255) comment 'Option key',
    OPTION_VALUE VARCHAR(1023) comment 'Option value',

    DELETED  TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED  TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Sys option table' charset = utf8;