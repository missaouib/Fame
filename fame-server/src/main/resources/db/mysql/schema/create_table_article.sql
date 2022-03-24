create table if not exists ARTICLE
(
    ID            INT auto_increment
    primary key comment 'ID',
    TITLE         VARCHAR(255)                         not null comment 'Title',
    CONTENT       MEDIUMTEXT                           not null comment 'Content',
    AUTHOR_ID     INT                                  not null comment 'Author id',
    HITS          INT        default 0                 not null comment 'number of hits',
    STATUS        VARCHAR(32)                          not null comment 'status',
    LIST_SHOW     TINYINT(1) default 1                 not null comment 'Whether to show in the list',
    HEADER_SHOW   TINYINT(1) default 0                 not null comment 'whether to show at the top',
    ALLOW_COMMENT TINYINT(1) default 1                 not null comment 'allow comment',
    PRIORITY      INT        default 0                 not null comment 'priority, reverse',
    PUBLISH_TIME  TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'publish time',

    DELETED       TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED       TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED      TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
    ) comment 'Article table' charset = utf8;