create table if not exists ARTICLE_CATEGORY
(
    ID          INT auto_increment
        primary key comment 'ID',
    ARTICLE_ID  INT                                  not null comment 'article id',
    CATEGORY_ID INT                                  not null comment 'category id',
    DELETED     TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED     TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED    TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Article-Category Association Table' charset = utf8;