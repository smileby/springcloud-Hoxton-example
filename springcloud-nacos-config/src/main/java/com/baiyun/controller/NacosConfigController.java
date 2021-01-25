package com.baiyun.controller
        ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("nacos/config")
@RefreshScope // 通过 Spring Cloud 原生注解 @RefreshScope 实现配置自动更新：
public class NacosConfigController {

    @Value("ftp.config.address")
    private String address;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;


    /**
     * 方式一：
     *      通过注解的方式获取Nacos-config中的属性
     * @return
     */
    @RequestMapping("getAddress")
    public String getAddress(){
        System.out.println(address);
        return address;
    }

    /**
     * 方式二： 硬编码的方式
     * @return
     */
    @RequestMapping("getUserName")
    public String getUserName(){
        String username = configurableApplicationContext.getEnvironment().getProperty("ftp.config.username");
        System.out.println(username);
        return username;
    }
}
