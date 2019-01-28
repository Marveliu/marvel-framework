# Marvel-Framework

[![standard-readme compliant](https://img.shields.io/badge/standard--readme-OK-green.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

:book:轻量级MVC框架，集成shiro，实现了AOP，IOC等特性，注释清晰。[参考](https://gitee.com/huangyong/smart-framework)《架构探险-从零开始搭建java框架》

# 说明

```shell
# 框架核心
├── marvel-framework-core
# 集成demo
├── marvel-framework-demo
# servlet使用
├── marvel-framework-servlet
# 集成shiro的权限插件
├── marvel-plugin-security
```

# 使用

1. 创建数据库：marvel-framework
2. 打包

```shell
# install core
cd marvel-framework-core  
mvn clean install -DskipTests
# 生成war包
cd ..
mvn clean package -DskipTests
```

# 演示

![请求](http://marveliu-md.oss-cn-beijing.aliyuncs.com/md/2019-01-28-084948.png)

```shell
DEBUG com.marvel.aspect.ControllerAspect - ---------- begin ----------
DEBUG com.marvel.aspect.ControllerAspect - class: com.marvel.controller.CustomerController
DEBUG com.marvel.aspect.ControllerAspect - method: show
DEBUG com.marvel.aspect.ControllerAspect - time: 64ms
DEBUG com.marvel.aspect.ControllerAspect - ----------- end -----------
```

MIT © 2018 Marveliu