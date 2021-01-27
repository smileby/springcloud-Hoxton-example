package com.baiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@NacosConfigurationProperties(dataId = "springcloud-nacos-config", autoRefreshed = true)
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConfigApp {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApp.class);
    }
}
