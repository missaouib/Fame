create table if not exists ARTICLE_TAG
(
    ID          INT auto_increment
        primary key comment 'ID',
    ARTICLE_ID  INT                                  not null comment 'Article Id',
    TAG_ID INT                                  not null comment 'Tag id',
    DELETED     TINYINT(1) default 0                 not null comment '1: deleted, 0: not',
    CREATED     TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED    TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Article-tag association table' charset = utf8;