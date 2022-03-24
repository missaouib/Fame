create table if not exists CATEGORY
(
    ID        INT auto_increment
        primary key comment 'ID',
    PARENT_ID INT comment 'Category parent id',
    NAME      VARCHAR(32)                          not null comment 'Category name',
    DELETED   TINYINT(1) default 0                 not null comment '1: deleted, 0: not',
    CREATED   TIMESTAMP  default CURRENT_TIMESTAMP not null comment 'creation time',
    MODIFIED  TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'modification time'
) comment 'Category table' charset = utf8;