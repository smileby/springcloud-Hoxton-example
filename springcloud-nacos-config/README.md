# springcloud-nacos-config
* Nacos配置中心
> 使用nacos-config作为配置中心

## 下载Nacos-server
> 没找到国内的下载链接, 在github下载的server包，有点慢，挂VPN还行
> startup.cmd 启动Nacos， 默认账号密码：nacos/nacos
> 访问地址： 127.0.0.1:8848/nacos/index.html

## 引入nacos-config配置依赖
```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
```

## 新建bootstrap.yml配置文件
```
server:
  port: 8004
spring:
  application:
    name: springcloud-nacos-config
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        enabled: true
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yaml  # 配置的格式,目前只支持 properties 和 yaml 类型
  profiles: dev  # 环境标识
  # 该配置文件对应配置中心的data-id为：springcloud-nacos-config-dev.yaml
```

## 在Nacos配置中心进行配置
> 配置管理 - 配置列表 - 新增
>> Data Id: 对应 springcloud-nacos-config-dev.yaml

### Data Id遵循以下规则
```
    // 和 Eureka 基本一个意思
    ${prefix}-${spring.profiles.active}.${file-extension}
    
```

| variable | value | desc |  
| :--- | :--- | :--- |   
| {prefix}| spring.application.name | 客户端服务名，可以通过spring.cloud.nacos.config.prefix来配置 |  
| {spring.profiles.active} | spring.profiles.active | 环境变量,注意：当 spring.profiles.active 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 ${prefix}.${file-extension} | 
| {file-extension} | spring.cloud.nacos.config.file-extension | 为配置内容的数据格式。 |  