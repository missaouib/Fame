# 升级须知

## V2.0.0

### 主要升级:

* fame-server 服务 orm 框架`SpringDataJpa` -> `Mybatis-Plus`

* fame-server `Article`、`Tag`、`Category` 数据模型关系优化

* mysql 版本 `5.7.x` -> `8.0.x`

* 相关依赖版本升级

### 升级事项

> **由于数据库版本升级，且数据结构有所变化，升级前请务必备份数据库数据。可参考[备份mysql容器的sql数据](/develop_guide?id=备份mysql容器的sql数据)**

执行升级sql:

```sql
-- 创建新表

create table article_category
(
    ID          int auto_increment comment 'ID'
        primary key,
    ARTICLE_ID  int                                 not null comment '文章ID',
    CATEGORY_ID int                                 not null comment '分类ID',
    DELETED     tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED     timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文章-分类关联表';

create table article_tag
(
    ID         int auto_increment comment 'ID'
        primary key,
    ARTICLE_ID int                                 not null comment '文章ID',
    TAG_ID     int                                 not null comment '标签ID',
    DELETED    tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文章-标签关联表';

create table category
(
    ID        int auto_increment comment 'ID'
        primary key,
    PARENT_ID int null comment '上级分类ID',
    NAME      varchar(32)                         not null comment '分类名称',
    DELETED   tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '分类表';

create table tag
(
    ID       int auto_increment comment 'ID'
        primary key,
    NAME     varchar(32)                         not null comment '标签名称',
    DELETED  tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '标签表';


-- 修改表

alter table log rename to sys_log;

alter table sys_log
    change TYPE LOG_TYPE varchar (32) not null comment '操作类型';

alter table article
    add LIST_SHOW tinyint(1) default 1 not null comment '是否在列表显示';

alter table article
    add HEADER_SHOW tinyint(1) default 0 not null comment '是否在顶部显示';

alter table article
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table comment
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table media
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table sys_option
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table sys_log
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table user
    add DELETED tinyint(1) default 0 not null comment '逻辑删除 0:未删除 1:已删除';

alter table article
    modify type varchar (32) null;

-- 迁移旧数据逻辑

insert into category
    (PARENT_ID, NAME)
select null, name
from meta
where type = 'category';

insert into tag
    (NAME)
select name
from meta
where type = 'tag';

insert into article_category
    (ARTICLE_ID, CATEGORY_ID)
select m.ARTICLE_ID, c.id
from meta me
         left join middle m on me.id = m.meta_id
         left join category c on c.NAME = me.name
where me.type = 'category';


insert into article_tag
    (ARTICLE_ID, TAG_ID)
select m.ARTICLE_ID, t.id
from meta me
         left join middle m on me.id = m.meta_id
         left join tag t on t.NAME = me.name
where me.type = 'tag';

update article
set DELETED = 1,
    STATUS='DRAFT'
where STATUS = 'DELETE';

update article
set LIST_SHOW   = 1,
    HEADER_SHOW = 0
where type = 'post';
update article
set LIST_SHOW   = 0,
    HEADER_SHOW = 1
where type = 'note';

update sys_log
set LOG_TYPE = 'ARTICLE'
where (LOG_TYPE = 'POST' or LOG_TYPE = 'NOTE');


-- 删除旧表和字段
-- 请确保服务正确运行后再执行!

-- drop table if exists meta;
-- drop table if exists middle;

-- alter table article drop column type;
-- alter table article drop column comment_count;
-- alter table article drop column category;
-- alter table article drop column tags;

-- alter table comment drop column status;

```




