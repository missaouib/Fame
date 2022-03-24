create table if not exists SYS_LOG
(
    ID       INT auto_increment
        primary key comment 'ID',
    DATA     TEXT comment 'Data',
    MESSAGE  VARCHAR(1023) comment 'Message',
    LOG_TYPE VARCHAR(32)                          not null comment 'Log Type',
    IP       varchar(255) comment 'ip address',
    USER_ID  INT comment 'User id',

    DELETED  TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED  TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Sys log table' charset = utf8;