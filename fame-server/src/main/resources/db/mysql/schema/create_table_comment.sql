create table if not exists COMMENT
(
    ID         INT auto_increment
        primary key comment 'ID',
    ARTICLE_ID INT                                  not null comment 'Article id',
    PARENT_ID  INT comment 'parent comment id',
    CONTENT    TEXT                                 not null comment 'comment',
    NAME       VARCHAR(255) comment 'Name',
    EMAIL      VARCHAR(255) comment 'Email',
    WEBSITE    VARCHAR(255) comment 'Website',
    AGREE      INT        default 0                 not null comment 'Number of approvals',
    DISAGREE   INT        default 0                 not null comment 'Number of non approvals',
    IP         VARCHAR(255) comment 'Reviewer IP',
    AGENT      VARCHAR(255) comment 'Reviewer Browser AGENT',

    DELETED    TINYINT(1) default 0                 not null comment '1: deleted, 0 not',
    CREATED    TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED   TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'comment table' charset = utf8;
