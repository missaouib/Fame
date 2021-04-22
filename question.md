# 常见问题

### mysql的数据存在哪里？如果docker镜像被删除我的数据会丢失吗？

  在`docker-compose.yml`中的`fame-mysql`配置可以看到有个配置:
  ```
  volumes:
    - ~/.fame/mysql/mysql_data:/var/lib/mysql
  ```
  将docker镜像中的文件夹挂载到外部系统的文件夹，所以即使docker镜像被删除或者重装mysql数据都不会丢失，在系统的`~/.fame/mysql/mysql_data`文件夹中。

### 在管理后台上传的图片资源文件放在哪里？如果docker镜像被删除我的数据会丢失吗？

  在`docker-compose.yml`中的`fame-server`配置可以看到有个配置:
  ```
      volumes:
    - ~/.fame/upload:/root/.fame/upload
  ```
  将docker镜像中的文件夹挂载到外部系统的文件夹，所以即使docker镜像被删除或者重装上传的资源文件数据都不会丢失，在系统的`~/.fame/upload`文件夹中。

### 我想要看系统的日志，在哪里能看到

  在系统的`~/.fame/logs/`文件夹下会有Fame博客生成的日志，其中nginx文件夹为nginx生成的日志，server文件夹为fame-server的SpringBoot生成的日志