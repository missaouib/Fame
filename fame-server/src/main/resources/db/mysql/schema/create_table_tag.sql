create table if not exists TAG
(
    ID        INT auto_increment
        primary key comment 'ID',
    NAME      VARCHAR(32)                          not null comment 'Tag name',
    DELETED  TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED  TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Tag table' charset = utf8;