create table if not exists USER
(
    ID           INT auto_increment
        primary key comment 'ID',
    USERNAME     VARCHAR(45)                          not null unique comment 'Unique user name',
    PASSWORD_MD5 VARCHAR(45)                          not null comment 'MD5 password',
    EMAIL        VARCHAR(45) comment 'Email',
    SCREEN_NAME  VARCHAR(45) comment 'User Screen name',
    LOGGED       TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'last login time',

    DELETED  TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED  TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'User table' charset = utf8;