<p align="center">
    <a href="https://github.com/SAWARATSUKI/ServiceLogos.git">
        <img src="./assets/Java.png" width="500">
    </a>
    <br />
    <br />
    <a href="https://choosealicense.com/licenses/lgpl-3.0/"><img src="https://img.shields.io/badge/license-MIT-red.svg?style=flat" alt="GNU General Public License v3.0"></a>
    <img src="https://github.com/ManimCommunity/manim/workflows/CI/badge.svg" alt="CI">
    <br />
    <br />
    <i>不要走远，希望就在附近</i>
</p>
<hr />

- [项目介绍](#项目介绍)
- [后端技术](#后端技术)
- [项目地址](#项目地址)
- [项目 API](#项目-api)
- [项目功能](#项目功能)
- [更新链接](#更新链接)
- [联系方式](#联系方式)
- [开源协议](#开源协议)

## 项目介绍
Ter 是一个练习项目，将目前主流的java开发技术进行整合，开发一个集在线聊天、弹幕视频的IM项目。\
通过netty实现和前端的websocket连接,包含登录认证、微信扫码登录、实时弹幕发送、消息列表、消息类型、评论、上传下载等功能。\
同时还有频控注解、aop日志注解、ip解析归属地等实用的小轮子。
## 后端技术
|         技术          | 说明                                       | 官网                                                         |
|:-------------------:| ------------------------------------------ | ------------------------------------------------------------ |
|    SpringBoot 3     | web开发必备框架                            | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|       MyBatis       | ORM框架                                    | http://www.mybatis.org/mybatis-3/zh/index.html               |
|   Spring Security   | 一个功能强大且高度可定制的身份验证和访问控制框架           | [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)               |
|        Redis        | 缓存加速，多数据结构支持业务功能           | [https://redis.io](https://redis.io)                         |
|      Caffeine       | 本地缓存                                   | http://caffe.berkeleyvision.org/                             |
|        Nginx        | 负载均衡，https配置，websocket升级，ip频控 | [https://nginx.org](https://nginx.org)                       |
|       Docker        | 应用容器引擎                               | [https://www.docker.com](https://www.docker.com)             |
|         Oss         | 对象存储                                   | [https://letsencrypt.org/](https://letsencrypt.org/)         |
|         Jwt         | 用户登录，认证方案                         | [https://jwt.io](https://jwt.io)                             |
|       Lombok        | 简化代码                                   | [https://projectlombok.org](https://projectlombok.org)       |
|       Hutool        | Java工具类库                               | https://github.com/looly/hutool                              |
|     Swagger-UI      | API文档生成工具                            | https://github.com/swagger-api/swagger-ui                    |
| Hibernate-validator | 接口校验框架                               | [hibernate.org/validator/](hibernate.org/validator/)         |
|        minio        | 自建对象存储                               | https://github.com/minio/minio                               |

## 项目地址
* 后端代码：https://github.com/xunlu129/teriteri-client.git

## 项目 API
本地项目启动后访问:http://localhost:6771/swagger-ui/index.html 
## 项目功能
* 用户模块
  * 用户注册，登录认证
* 视频模块
* 聊天模块

## 更新链接
[Github](https://github.com/21xiaoye/Ter.git)

## 联系方式
如果对你有帮助的话，可以Star一下吗？\
欢迎添加微信进行交流 \
<img src="./assets/wx.jpg" width="280">


## 开源协议
[GNU General Public License v3.0](./LICENSE)