create table if not exists MEDIA
(
    ID        INT auto_increment
        primary key comment 'ID',
    NAME      VARCHAR(255)                          not null comment 'media name',
    SUFFIX      VARCHAR(255)                          not null comment 'File extension',
    THUMB_URL      VARCHAR(255)                          not null comment 'Thumbnail path',
    URL      VARCHAR(255)                          not null comment 'file path',

    DELETED   TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED   TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED  TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'media table' charset = utf8;