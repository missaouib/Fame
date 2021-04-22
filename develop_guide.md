# 开发文档

## fame-server

fame-server是Fame博客系统的服务端，开发语言为Java，使用技术栈为

* SpringBoot
* Mybatis-Plus
* flexmark(Markdown解析)
* Mysql

### API接口文档

该服务为Restful接口，可以参考[接口文档](https://zzzzbw.github.io/Fame/swagger/)

## fame-front

fame-front是Fame博客的前端部分，由于前端博客有搜索引擎的SEO需求，而Vue等单页应用框架对搜索引擎收录不大友好，所以使用了前端框架Nuxt。Nuxt是SSR版的Vue，可以参考[官方](https://zh.nuxtjs.org/)。

### sitemap

支持sitemap功能

启动服务后访问`http://xx.xxx.xx.xx/sitemap.xml`或`http://localhost:3000/sitemap.xml`（开发模式启动）

### rss

支持rss功能

启动服务后访问`http://xx.xxx.xx.xx/feed.xml`或`http://localhost:3000/feed.xml`（开发模式启动）

## fame-admin

fame-admin是Fame博客的后台管理界面部分，使用了Vue+Elementui框架。

一些关于网站的系统设置如账号密码等可以在管理后台的`网站设置`中修改。

## fame-docker

fame-docker是关于docker部署的各个系统的`Dockerfile`和一些配置文件。

文件结构:

```
└─fame-docker
    ├─fame-admin          
      ├─fame-admin-Dockerfile       // fame-admin服务Dockerfile文件
      └─nginx.conf                  // fame-admin服务nginx配置文件
    ├─fame-nginx
      ├─nginx-Dockerfile            // nginx服务Dockerfile文件
      └─nginx.conf                  // nginx服务nginx配置文件
    ├─fame-mysql-Dockerfile         // fame-mysql服务Dockerfile文件
    ├─fame-front-Dockerfile         // fame-front服务Dockerfile文件
    ├─fame-server-Dockerfile        // fame-server服务Dockerfile文件
```

> `fame-server`和`fame-front`项目都自带了服务器，所以直接用docker直接启动项目即可。
> `fame-admin`项目是静态页面，需要搭配服务器使用，所以这个docker项目增加了一个nginx服务器。

## 备注

### 常用脚本

#### 进入各个容器(服务启动时)

fame-server:

```
docker exec -it fame-server bash
```

fame-mysql:

```
docker exec -it fame-mysql bash
```

fame-front:

```
docker exec -it fame-front sh
```

fame-admin:

```
docker exec -it fame-admin sh
```

fame-nginx:

```
docker exec -it fame-nginx sh
```

#### 备份mysql容器的sql数据

```
docker exec fame-mysql /usr/bin/mysqldump -u root --password=root fame > `date '+%Y-%m-%dT%H%M%S'`backup.sql
```