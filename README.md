# 部署方式

> 博客管理后台初始的账号：fame，密码：123456，登录管理后台后可修改。

## Docker方式部署(推荐)

首先保证有Docker和Docker
compose的环境，如果没有可参考[这里](https://github.com/zzzzbw/Fame/wiki/Docker%E5%92%8CDocker-compose%E5%AE%89%E8%A3%85)

1. 克隆项目到本地

   ```
   git clone https://github.com/zzzzbw/Fame.git
   ```

2. 启动项目

    ```
    docker-compose up 或 docker-compose up -d
    ```
   第一次启动推荐`docker-compose up`，可以看到启动日志，由于要下载镜像和maven依赖，时间可能较久，视网络环境和性能而定

    ```
    [root@localhost Fame]# docker-compose up -d
    Starting fame-front ... 
    Starting fame-admin ... 
    Starting fame-front ... done
    Starting fame-admin ... done
    Starting fame-nginx ... done
    ```

3. 访问地址

   启动完成后，在浏览器访问

   `http://xx.xxx.xx.xx/` 为博客前端首页

   `http://xx.xxx.xx.xx/admin` 为博客管理后台首页

   > xx.xxx.xx.xx为你的域名或者ip地址，本地的话即为127.0.0.1

## 手动前后端分步部署

> 以下以Centos7为例

> 首先保证服务器拥有`java8` `mysql8.0.x` `maven3.3.x` `node10.x` `npm6.x`的环境（版本不一定要完全一样，但避免奇怪的问题出现，最好相同），并且已经安装了`git`。

### 编译项目

> 如果不想手动编译项目可直接跳到[部署项目](#部署项目)

1. 克隆项目到本地

   ```
   git clone https://github.com/zzzzbw/Fame.git && cd Fame
   ```

2. 编译后端服务(fame-server)

  ```
    cd fame-server
    mvn -DskipTests=true package
  ```

编译完成后`target/Fame-x.x.jar`即为后端服务包

3. 编译前端博客页面服务(fame-front)

  ```
    cd fame-front
    npm install && npm run build
  ```

`.nuxt`为编译后的文件，但启动服务也需要`config``plugins``nuxt.config.js``package.json`，所以要移动文件的话不要忘了这几个一起移动。

4. 编译前端后台管理服务(fame-admin)

  ```
    cd fame-admin
    npm install && npm run build
  ```

编译完成后dist文件夹即为前端后台管理页面文件

### 部署项目

1. 下载编译完成文件(如果执行了上面手动编译则可以跳过这步)

   ```
   wget https://github.com/zzzzbw/Fame/releases/download/v2.0.0/Fame-release.tar.gz
   tar zxvf Fame-release.tar.gz
   ```

2. 启动后端服务器(fame-server)

   2.1 进入文件夹

   `cd fame-server`

   2.2 创建spring-boot生产环境配置文件

   `vi src/main/resources/application-prod.yml`

    ```yaml
   #datasource
   spring:
     datasource:
       type: com.zaxxer.hikari.HikariDataSource
       driverClassName: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/fame?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
       username: root
       password: root
       schema: classpath:db/mysql/schema/*.sql
       # 第三方连接池需要设置为 always
       initialization-mode: always
   #log
   logging:
     level:
       root: INFO
       com:
         zbw: INFO
     file:
       name: log/fame.log
    ```

   将配置中的`username`和`password`修改成对应你数据库的用户名密码。如果mysql不是安装在同一个服务器或者端口不是3306，则要对应修改`url`。

   日志文件会保存在`项目/fame-server/log`文件夹下，如果想要修改日志保存目录则修改`file`。

   2.3 启动项目

   `nohup java -jar Fame-1.0.jar --spring.profiles.active=prod`

   当执行`netstat -ntulp | grep 9090`有列出线程时，说明启动完成。

3. 启动前端博客页面服务(fame-front)

   3.1 进入文件夹

   `cd fame-front`

   3.2 安装依赖

   `npm install`

   3.3 启动服务

   `nohup npm run start`

   当执行`netstat -ntulp | grep 3000`有列出线程时，说明启动完成。


4. 配置nginx及前端后台管理服务(fame-admin)

   > 由于fame-admin项目是静态页面，必须要依靠服务器来使用，所以这里和nginx配置一起启动

   4.1 设置nginx配置

    ```
    # 反向代理server配置
    server {
      listen       80;
      charset utf-8;
      
      location /api/ {
        proxy_set_header   X-Real-IP $remote_addr; #转发用户IP
        proxy_pass http://127.0.0.1:9090; #fame-server
      }
       
      location /media/ {
        proxy_pass http://127.0.0.1:9090; #fame-server 资源文件
      }
       
      location /admin {
        proxy_pass   http://127.0.0.1:3001/; #fame-admin
      }
       
      location / {
        proxy_pass   http://127.0.0.1:3000/; #fame-front nuxt项目 监听端口
      }
       
      error_page   500 502 503 504  /50x.html;
      location = /50x.html {
        root   /usr/share/nginx/html;
      }
    }
      
    # fame-admin server配置
    server {
      listen 3001;
      charset utf-8;
      
      location / {
        root  /usr/share/nginx/fame-admin/;
        try_files $uri $uri/ /index.html;
      }
    }
    ```

   4.2 拷贝fame-admin文件到nginx目录

   `cp -r fame-admin/dist/ /usr/share/nginx/fame-admin/`

   > 这里拷贝的目标文件夹要和nginx配置文件中的fame-admin server配置里的root目录一致

   然后启动nginx即部署完成

   `systemctl start nginx.service`

## 开发环境启动

首先保证有 `java8` `mysql8.0.x` `maven3.3.x` `node10.x` `npm6.x`的环境(版本不一定要完全一样，但避免奇怪的问题出现，最好相同)

1. 克隆项目到本地

   ```
   git clone https://github.com/zzzzbw/Fame.git
   ```

2. 启动服务端 (项目使用lombok插件，如果要在ide中调试要有lombok插件)

   2.1 进入服务端文件夹

        `cd fame-server`

   2.2 修改spring-boot配置文件

   `vi src/main/resources/application-dev.yml`

      ```
      spring:
        datasource:
          driverClassName: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/fame?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: root
      ```
   将配置中的`username`和`password`修改成对应你数据库的用户名密码

   2.3 启动fame-server

   `mvn clean spring-boot:run -Dmaven.test.skip=true`

3. 启动博客前端

   3.1 进入前端文件夹

   `cd fame-front`

   3.2 安装依赖和启动服务

      ```
    npm install
    npm run dev
      ```

   3.3 访问地址

   启动完成后，浏览器访问 `http://localhost:3000`

4. 启动博客管理后台

   4.1 进入后端文件夹

   `cd fame-admin`

   4.2 安装依赖和启动服务

     ```
    npm install
    npm run serve
     ```

   4.3 访问地址

   启动完成后，浏览器访问 `http://localhost:8010/admin`